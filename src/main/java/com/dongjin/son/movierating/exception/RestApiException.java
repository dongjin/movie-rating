package com.dongjin.son.movierating.exception;

import org.springframework.http.HttpStatus;

public interface RestApiException {

    /**
     * Get HttpStatus
     * 
     * @return httpStatus
     */
    HttpStatus getHttpStatus();
    
    /**
     * Get Error Code
     * 
     * @return short string error code
     */
    
    String getErrorCode();    
    
    
    /**
     * Get Description
     * 
     * @return description of error
     */
    
    String getDescription();
        
}
