package com.icia.pboard.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.icia.pboard.dao.AuthorityDao;
import com.icia.pboard.dao.UserDao;
import com.icia.pboard.dto.UserDto;
import com.icia.pboard.dto.UserDto.ChangePassword;
import com.icia.pboard.dto.UserDto.Update;
import com.icia.pboard.entity.Level;
import com.icia.pboard.entity.User;
import com.icia.pboard.exception.JobRestFailException;
import com.icia.pboard.exception.RestUserNotFoundException;
import com.icia.pboard.util.MailUtil;

@Service
public class UserRestService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private MailUtil mailUtil;
	@Autowired
	private	AuthorityDao authorityDao;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("c:/upload/profile")
	private String profileFolder;
	
	public void idAvailableCheck(String username) {
		User user = userDao.findById(username);
		if(user!=null)
			throw new JobRestFailException("사용중인 아이디입니다.");
	}

	public void emailAvailableCheck(String email) {
		User user = userDao.findByEmail(email);
		if(user!=null)
			throw new JobRestFailException("이메일이 중복됩니다.");
	}
	
	public void join(UserDto.Join dto, MultipartFile profile) {
		User user = modelMapper.map(dto, User.class);
		if(profile!=null && profile.isEmpty()==false) {
			String profileFileName = user.getUsername() + ".jpg";
			File file = new File(profileFolder, profileFileName);
			try {
				FileCopyUtils.copy(profile.getBytes(), file);
				user.setProfile(profileFileName);
			} catch (IOException e) {
				user.setProfile("default.jpg");
				e.printStackTrace();
			}
		} else 
			user.setProfile("default.jpg");
		String checkCode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword).setCheckCode(checkCode).setLevels(Level.NORMAL);
		
		userDao.insert(user);
		authorityDao.insert(user.getUsername(), "ROLE_USER");
		
		mailUtil.SendJoinCheckMail("admin@icia.co.kr", user.getEmail(), checkCode);
	}
	
	@Scheduled(cron ="0 0 4 ? * THU")
	public void deleteUncheckUser() {
		userDao.deleteByCheckCodeIsNotNull();
	}

	public String findId(String email) {
		User user = userDao.findByEmail(email);
		if(user==null)
			throw new RestUserNotFoundException();
		return user.getUsername();
	}

	public void changeIrum(String irum, String username) {
		if(userDao.findById(username)==null)
			throw new RestUserNotFoundException();
		userDao.update(User.builder().username(username).irum(irum).build());
	}

	public void changePassword(UserDto.ChangePassword dto, String username) {
		User user = userDao.findById(username);
		if(user==null)
			throw new RestUserNotFoundException();
		
		if(passwordEncoder.matches(dto.getPassword(), user.getPassword())==false)
			throw new JobRestFailException("비밀번호를 확인하지 못했습니다.");
		
		String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword());
		userDao.update(User.builder().username(username).password(newEncodedPassword).build());
	}
	
	public void update(UserDto.Update dto, MultipartFile profile, String username) {
		User result = userDao.findById(username);
		if(result==null)
			throw new RestUserNotFoundException();
		
		User param = modelMapper.map(dto, User.class).setUsername(username);
		
		if(profile!=null && profile.isEmpty() ==false) {
			String profileFileName = username +".jpg";
			File file = new File(profileFolder, profileFileName);
			try {
				FileCopyUtils.copy(profile.getBytes(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(result.getProfile().equals("default.jpg")) {
				param.setProfile(profileFileName);
			}
		}
		if(dto.getPassword()!=null) {
			if(dto.getNewPassword()==null) 
				param.setPassword(null);
			else {
				String encodedPassword = result.getPassword();
				if(passwordEncoder.matches(param.getPassword(), encodedPassword)==false) 
					throw new JobRestFailException("비밀번호를 확인하지 못했습니다.");
				param.setPassword(passwordEncoder.encode(param.getPassword()));
			}
		}
		userDao.update(param);
	}
}
