package com.dongjin.son.movierating.exception;

import org.springframework.http.HttpStatus;

import com.dongjin.son.movierating.exception.RestApiException;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException implements RestApiException  {
 
    private static final long serialVersionUID = 1L;
    
    private String description;
   
    public MovieNotFoundException(Integer id) {          
        this(id, null);             
    }
    
    public MovieNotFoundException(Integer id, String description) {  
        super(id.toString());
        
        if (description!=null && description.length() >0) {
            this.description = description;              
        }
        else {
            this.description = "movie is not found by id: " + id;
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