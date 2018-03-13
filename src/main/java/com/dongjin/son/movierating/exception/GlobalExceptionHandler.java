package com.dongjin.son.movierating.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dongjin.son.movierating.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
// @RequiredArgsConstructor
public class GlobalExceptionHandler {


    // when required field is not available : E.g., title for CreateMovieDto
    
    //----------------------------------------------------------------------
    // Exception Handlers for Spring Boot Generated Exceptions
    //----------------------------------------------------------------------
   
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleAll(HttpMessageNotReadableException e) {
        log.error("{}", "HttpMessageNotReadableException.");        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(HttpStatus.BAD_REQUEST,
                                                     "message_not_readable",
                                                     "http message is not readable"));
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(MethodArgumentTypeMismatchException e) {
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(HttpStatus.BAD_REQUEST,
                                                     "invalid_path_parameter",
                                                     e.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(HttpMediaTypeNotSupportedException e) {
        
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                             .body(new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                                                     "unsupported_media_type",
                                                     e.getMessage()));
    }

    // cannot find endpoint supporting requested media type : cannot produce request format!
  
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(HttpMediaTypeNotAcceptableException e) {
        
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                             .body(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE,
                                                     "not_acceptable_mediatype",
                                                     e.getMessage()));
    }
    
    
    @ExceptionHandler({MovieNotFoundException.class, RatingNotFoundException.class, BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RestApiException e) {

        log.error("{}", "RestApi exception occurred");        
//        e.printStackTrace();
        
        return ResponseEntity.status(e.getHttpStatus())
                             .body(new ErrorResponse(e));
    }
    
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {

        log.error("{}", "Unhandled exception occurred");        
        e.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                                     "internal_server_error",
                                                     "An unknown error has caused the request to fail"));
    }
    
    
    
    /*
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(MovieNotFoundException e) {

        log.error("{}", "BadRequestException exception occurred");        
        
//        e.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                                                     "not_found",
                                                     e.getDescription()));
    }
    
    
    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRatingNotFoundException(RatingNotFoundException e) {

        log.error("{}", "BadRequestException exception occurred");        
        
//        e.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                                                     "not_found",
                                                     e.getDescription()));
    }
    
    
    
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(HttpClientErrorException e) {

        log.error("{}", "HttpClientErrorException exception occurred");        
        
//        e.printStackTrace();
        
        return ResponseEntity.status(e.getStatusCode())
                             .body(new ErrorResponse(e.getStatusCode().value(),
                                                     e.getMessage(),
                                                     e.getLocalizedMessage()));
    }
    */

}
