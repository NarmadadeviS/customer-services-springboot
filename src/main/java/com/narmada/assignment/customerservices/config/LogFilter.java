package com.narmada.assignment.customerservices.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Log request details
            logRequest(wrappedRequest);
            // Log response details
            logResponse(wrappedResponse, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder log = new StringBuilder();
        log.append("\n---[HTTP REQUEST]---\n");
        log.append(request.getMethod()).append(" ").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            log.append("?").append(request.getQueryString());
        }

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (!requestBody.isBlank()) {
            log.append("Body:\n").append(requestBody).append("\n");
        }
        log.append("\n---------------------");

        logger.info(log.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        StringBuilder log = new StringBuilder();
        log.append("\n---[HTTP RESPONSE]---\n");
        log.append("Status: ").append(response.getStatus()).append("\n");

        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (!responseBody.isBlank()) {
            log.append("Body:\n").append(responseBody).append("\n");
        }

        log.append("Time Taken: ").append(duration).append(" ms");
        log.append("\n---------------------");

        logger.info(log.toString());
    }
}
