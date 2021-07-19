package com.icia.pboard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.icia.pboard.dao.UserDao;
import com.icia.pboard.entity.Level;
import com.icia.pboard.entity.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class UserDaoTest {
	@Autowired
	private UserDao dao;
	
//	@Test
	public void initTest() {
		assertThat(dao, is(notNullValue()));
	}
	
//	@Test
	public void insertTest() {
		User user = new User("SPRING33","12341234","홍길동","spring@naver.com",LocalDate.now(),LocalDate.now(),
				0, 0, Level.NORMAL, false, "SPRING22.JPG", "1234");
		assertThat(dao.insert(user), is(1));
	}
	
//	@Test
	public void findTest() {
		assertThat(dao.findById("SPRING33").getLevels(), is(Level.NORMAL));
		assertThat(dao.findByEmail("spring@naver.com").getLevels(), is(Level.NORMAL));
		assertThat(dao.findByCheckCode("1234").getLevels(), is(Level.NORMAL));
	}
	
//	@Transactional
//	@Test
	public void update() {
		// 1. 체크코드를 확인 -> 체크코드 삭제, enabled를 변경
		assertThat(dao.update(User.builder().username("SPRING33").enabled(true).checkCode("").build()), is(1));
		
		// 2. 비밀번호 변경
		assertThat(dao.update(User.builder().username("SPRING33").password("1234").build()), is(1));
		
		// 3. 내정보 변경 : 비밀번호, 이메일, 프사
		User user = User.builder().username("SPRING33").email("spring@naver.com").profile("spring22.jpg").build();
		assertThat(dao.update(user), is(1));
		
		// 4. 로그인 실패 횟수 변경
		assertThat(dao.update(User.builder().username("SPRING33").loginFailureCnt(3).build()), is(1));
		
		// 5. 레벨 변경
		assertThat(dao.update(User.builder().username("SPRING33").levels(Level.SILVER).build()), is(1));
		
		// 6. 글쓴 횟수 변경
		assertThat(dao.update(User.builder().username("SPRING33").writeCnt(3).build()), is(1));
	}
	
//	@Transactional
//	@Test
	public void deleteById() {
		assertThat(dao.delete("SPRING33"), is(1));
	}
	
//	@Test
//	@Transactional
	public void deleteByCheckCodeIsNotNullTest() {
		assertThat(dao.deleteByCheckCodeIsNotNull(), is(1));
	}
	
	
}
