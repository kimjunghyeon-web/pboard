package com.icia.pboard.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// private BoardJob() {
// } 만드는것
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardJob {
	public static class MvcUserNotFoundException extends RuntimeException {
		
	}
}
