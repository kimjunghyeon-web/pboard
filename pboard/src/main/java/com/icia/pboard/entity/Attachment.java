package com.icia.pboard.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {
	// ano로 첨부파일 삭제 -> bno로 첨부파일 읽어서 화면 업데이트
	private Integer ano;
	private Integer bno;
	// 원래 파일명 : 자기고과.hwp
	private String originalFileName;
	// 저장 파일명 : 202103261141-자기고과.hwp
	private String saveFileName;
	
	// 첨부파일 삭제할 때 글작성자가 맞는지 확인할 용도
	private String writer;
	// 파일크기
	private Long length;
	// 이미지 여부 : 첨부파일을 클릭하면 이미지라면 보여주고, 이미지가 아니라면 다운로드
	private Boolean isImage;
}
