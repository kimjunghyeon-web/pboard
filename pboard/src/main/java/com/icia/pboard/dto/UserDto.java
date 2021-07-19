package com.icia.pboard.dto;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.icia.pboard.entity.Level;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
	@Data
	public static class Join {
	@Pattern(regexp = "^[0-9A-Z]{8,10}$")
	private String username;
	private String password;
	private String irum;
	private String email;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	}
	
	@Data
	public static class ResetPassword {
		private String username;
		private String email;
	}
	
	@Data
	public static class Info {
		private String irum;
		private String email;
		private String joindayString;
		private Long day;
		private String birthdayString;
		private Integer writeCnt;
		private String level;
		private String profile;
	}
	
	@Data
	public static class Update {
		private String irum;
		private String email;
		private String password;
		private String newPassword;
	}
	
	@Data
	public static class ChangePassword {
		private String password;
		private String newPassword;
	}
}
