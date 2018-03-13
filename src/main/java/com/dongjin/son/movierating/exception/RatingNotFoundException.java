package com.dongjin.son.movierating.exception;

import org.springframework.http.HttpStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class RatingNotFoundException extends RuntimeException implements RestApiException{
 
    private static final long serialVersionUID = 1L;
    
    private String description;
   
    public RatingNotFoundException(Integer id) {          
        this(id, null);             
    }
    
    public RatingNotFoundException(Integer id, String description) {  
        super(id.toString());
        
        if (description!=null && description.length() >0) {
            this.description = description;              
        }
        else {
            this.description = "rating is not found by id: " + id;
        }
    }   

    @Override
    public HttpStatus getHttpStatus() {     
        return HttpStatus.NOT_FOUND;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getErrorCode() {
        return HttpStatus.NOT_FOUND.toString();
    }
}