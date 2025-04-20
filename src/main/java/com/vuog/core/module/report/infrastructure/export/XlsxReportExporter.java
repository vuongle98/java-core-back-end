//package com.vuog.core.module.report.infrastructure.export;
//
//import com.vuog.core.module.report.domain.model.ReportRequest;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//
//@Component
//public class XlsxReportExporter implements ReportExporter {
//
//    @Override
//    public InputStream exportReport(ReportRequest request) {
//        // XLSX generation logic using Apache POI
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Report");
//
//        // Create header row
//        Row headerRow = sheet.createRow(0);
//        List<String> fields = request.getFields();
//        for (int i = 0; i < fields.size(); i++) {
//            headerRow.createCell(i).setCellValue(fields.get(i));
//        }
//
//        // Add rows of data
//        List<Map<String, Object>> results = fetchReportData(request);
//        for (int i = 0; i < results.size(); i++) {
//            Row row = sheet.createRow(i + 1);
//            Map<String, Object> rowData = results.get(i);
//            for (int j = 0; j < fields.size(); j++) {
//                row.createCell(j).setCellValue(rowData.get(fields.get(j)).toString());
//            }
//        }
//
//        // Write to byte stream
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            workbook.write(baos);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new ByteArrayInputStream(baos.toByteArray());
//    }
//
//    private List<Map<String, Object>> fetchReportData(ReportRequest request) {
//        // Query and fetch data (implement similar to your previous solution)
//        return null;  // Replace with actual data fetch logic
//    }
//}