package com.icia.pboard.entity;

import java.time.*;
import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Builder
public class Board {
	private int bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private int readCnt;
	private int attachmentCnt;
	private int commentCnt;
	private int deleteCommentCnt;
	private int goodCnt;
	private int badCnt;
}
