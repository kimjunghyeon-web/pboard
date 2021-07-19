package com.icia.pboard.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.icia.pboard.dao.UserDao;
import com.icia.pboard.dto.UserDto;
import com.icia.pboard.dto.UserDto.ResetPassword;
import com.icia.pboard.entity.User;
import com.icia.pboard.exception.BoardJob;
import com.icia.pboard.exception.JobMvcFailException;
import com.icia.pboard.exception.PasswordCheckFailException;
import com.icia.pboard.util.MailUtil;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailUtil mailUtil;
	@Autowired
	private ModelMapper modelMapper;
	@Value("http://localhost:8081/profile/")
	private String profilePath;

	public void joinCheck(String checkCode) {
		User result = userDao.findByCheckCode(checkCode);
		if (result == null)
			throw new JobMvcFailException("잘못된 체크 코드입니다.");
		User param = User.builder().username(result.getUsername()).enabled(true).checkCode("1").build();
		userDao.update(param);
	}

	public void resetPwd(UserDto.ResetPassword dto) {
		User result = userDao.findById(dto.getUsername());
		if (result == null)
			throw new BoardJob.MvcUserNotFoundException();
		if (result.getEmail().equals(dto.getEmail()) == false)
			throw new JobMvcFailException("이메일을 확인할 수 없습니다.");

		String password = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(password);
		User param = User.builder().username(dto.getUsername()).password(encodedPassword).build();

		mailUtil.sendResetPasswordMail("admin@icia.co.kr", dto.getEmail(), password);
	}

	public UserDto.Info info(String username) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		User user = userDao.findById(username);
		UserDto.Info dto = modelMapper.map(user, UserDto.Info.class);

		dto.setLevel(user.getLevels().name());
		dto.setBirthdayString(dtf.format(user.getBirthday()));
		dto.setJoindayString(dtf.format(user.getJoinday()));

		// ChronoUnit은 날짜를 가지고 여러가지 계산을 수행하는 클래스
		dto.setDay(ChronoUnit.DAYS.between(user.getJoinday(), LocalDate.now()));
		// default.jpg -> localhost:8081/profile/default.jpg
		dto.setProfile(profilePath + user.getProfile());
		return dto;
	} 

	public void passwordCheck(String password, String username) {
		User user = userDao.findById(username);
		String encodedPassword = user.getPassword();
		if (passwordEncoder.matches(password, encodedPassword) == false)
			throw new PasswordCheckFailException("비밀번호를 정확히 입력하세요");
	}
	
	
	public void resign(String username) {
		userDao.delete(username);
	}
}
