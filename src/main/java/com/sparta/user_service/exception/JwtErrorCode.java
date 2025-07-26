package com.sparta.user_service.exception;

import org.springframework.http.HttpStatus;

public enum JwtErrorCode implements ErrorCode {
	INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_DENIED("접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
	;

	private final String message;
	private final HttpStatus status;

	JwtErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
