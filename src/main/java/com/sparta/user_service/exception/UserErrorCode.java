package com.sparta.user_service.exception;

import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
	USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", HttpStatus.BAD_REQUEST),
	INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	ACCESS_DENIED("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
	;

	private final String message;
	private final HttpStatus status;

	UserErrorCode(String message, HttpStatus status) {
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
