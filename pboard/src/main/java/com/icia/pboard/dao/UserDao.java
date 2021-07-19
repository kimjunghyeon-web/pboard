package com.icia.pboard.dao;

import com.icia.pboard.entity.User;

public interface UserDao {
	public User findById(String username);
	
	public User findByEmail(String email);

	public User findByCheckCode(String checkCode);
	
	public int insert(User user);
	
	public int update(User user);
	
	public int delete(String username);
	
	public int deleteByCheckCodeIsNotNull();
}
