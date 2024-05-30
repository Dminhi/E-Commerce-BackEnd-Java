package com.ra.model.dto.responsewapper;

import org.springframework.http.HttpStatus;

public class APIError {
    private HttpStatus status;
    private String message;
}
