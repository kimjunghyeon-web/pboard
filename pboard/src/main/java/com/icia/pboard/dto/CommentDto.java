package com.icia.pboard.dto;

import lombok.Data;

public class CommentDto {
	@Data
	public static class Read {
		private Integer cno;
		private Integer bno;
		private String writer;
		private Boolean isWriter;
		private String content;
		private String writeTimeString;
		private String profile;
	}
}
