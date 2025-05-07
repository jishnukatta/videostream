package com.videostreaming.authenticationservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().
		forEach(err -> errors.put(err.getField(),err.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errors);

	}
	
	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<?> handlePasswordMismatchException(PasswordMismatchException ex)
	{
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	
	@ExceptionHandler(AdminLoginException.class)
	public ResponseEntity<?> handleAdminLoginException(AdminLoginException ex)
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
	
	
	

}
