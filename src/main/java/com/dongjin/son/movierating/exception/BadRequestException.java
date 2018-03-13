package com.dongjin.son.movierating.exception;

import org.springframework.http.HttpStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException implements RestApiException{
	
	private static final long serialVersionUID = 1L;
	private String description;
	
	public BadRequestException(String msg) {
		super(msg);
	}
	
	public BadRequestException(String msg, String description) {  
	    super(msg);

	    if (description!=null && description.length() >0) {
	        this.description = description;              
	    }
	    else {
	        this.description = "bad request";
	    }
	}

	@Override
	public HttpStatus getHttpStatus() {	    
	    return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getDescription() {
	    return description;
	}

    @Override
    public String getErrorCode() {
        return HttpStatus.BAD_REQUEST.toString();
    }

  
}