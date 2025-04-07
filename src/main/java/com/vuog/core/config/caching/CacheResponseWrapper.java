package com.vuog.core.config.caching;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CacheResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream captureStream = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public CacheResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // No-op
                }

                @Override
                public void write(int b) throws IOException {
                    captureStream.write(b); // Capture data
                    CacheResponseWrapper.super.getOutputStream().write(b); // Write to original response
                }
            };
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(captureStream, true); // Use auto-flush
        }
        return writer;
    }

    public String getResponseBody() {
        try {
            getWriter().flush(); // Flush writer content to the stream
            return captureStream.toString(getCharacterEncoding()); // Convert stream content to string
        } catch (IOException e) {
            throw new IllegalStateException("Failed to capture response body", e);
        }
    }
}