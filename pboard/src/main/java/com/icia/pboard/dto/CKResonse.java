package com.icia.pboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CKResonse {
	private int uploaded;
	private String fileName;
	private String url;
}
