package com.hthayer.todoAngular.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NotFoundException( ) {
		super( );
	}
	
	public NotFoundException( String cause ) {
		super( cause );
	}
}