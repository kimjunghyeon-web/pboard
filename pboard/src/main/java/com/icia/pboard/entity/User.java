package com.icia.pboard.entity;

import java.time.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class User {
	private String username;
	private String password;
	private String irum;
	private String email;
	private LocalDate joinday;
	@DateTimeFormat(pattern ="yyyy-MM-dd")
	private LocalDate birthday;
	private Integer loginFailureCnt;
	private Integer writeCnt;
	private Level levels;
	private Boolean enabled;	// number(1), char(1)
	private String profile;
	private String checkCode;	// 가입 후 메일로 전송할 체크 코드
}
