package com.sparta.user_service.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getStatus();
	String getMessage();
}
