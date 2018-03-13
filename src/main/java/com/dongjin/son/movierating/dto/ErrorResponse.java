package com.dongjin.son.movierating.dto;

import org.springframework.http.HttpStatus;

import com.dongjin.son.movierating.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Resource class mapping to fields needed to create a new user.
 */
@Value
@RequiredArgsConstructor
public class ErrorResponse {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String description;

    public ErrorResponse(RestApiException e) {
        this.httpStatus = e.getHttpStatus();
        this.errorCode = e.getErrorCode();
        this.description = e.getDescription();
    }
}
