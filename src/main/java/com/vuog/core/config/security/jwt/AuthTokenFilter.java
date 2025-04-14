package com.vuog.core.config.security.jwt;

import com.vuog.core.common.util.Context;
import com.vuog.core.common.util.JwtUtils;
import com.vuog.core.module.auth.application.service.TokenService;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = jwtUtils.parseJwt(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);

            if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token and check if it is not blacklisted and is of correct type
                if (jwtUtils.isTokenValid(jwt, userDetails) && !tokenService.isTokenBlacklisted(jwt) && tokenService.checkTokenType(jwt, Token.TokenType.ACCESS)) {

                    if (jwtUtils.isTokenExpired(jwt)) {
                        Context.setSystemUser();  // Set the default user for audit logs
                        tokenService.blacklist(jwt);
                        Context.clear();
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException e) {
            // If the token is expired, return 401 Unauthorized
            Context.setSystemUser();  // Set the default user for audit logs
            tokenService.blacklist(jwt);
            Context.clear();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            return;
        } catch (Exception e) {
            // Handle other exceptions (e.g., invalid token, etc.)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
