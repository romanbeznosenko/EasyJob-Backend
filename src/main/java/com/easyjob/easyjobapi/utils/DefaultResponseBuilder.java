package com.easyjob.easyjobapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class DefaultResponseBuilder {

    public static void sendCustomResponse(HttpServletResponse response, HttpServletRequest request, int status, HttpStatus code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = Map.of(
                "status", status,
                "error", code.getReasonPhrase(),
                "message", message,
                "path", request.getRequestURI()
        );

        String jsonResponseString = new ObjectMapper().writeValueAsString(jsonResponse);
        response.getWriter()
                .write(jsonResponseString);
    }
}
