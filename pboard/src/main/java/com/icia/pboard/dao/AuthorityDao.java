package com.icia.pboard.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AuthorityDao {
	@Insert("insert into authorities(username, authority) values(#{username},#{authority})")
	public void insert(@Param("username") String username, @Param("authority") String authority);

}
