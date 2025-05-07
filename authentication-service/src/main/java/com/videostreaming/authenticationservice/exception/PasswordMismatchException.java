package com.videostreaming.authenticationservice.exception;

public class PasswordMismatchException extends IllegalArgumentException {

	public PasswordMismatchException(String msg) {
        super(msg);
    }
}
