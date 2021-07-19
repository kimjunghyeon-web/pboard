package com.icia.pboard.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
	private Integer cno;
	private Integer bno;
	private String writer;
	private String content;
	
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime writeTime;
	private String profile;
}
