package com.icia.pboard.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobMvcFailException extends RuntimeException {
	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}

}
