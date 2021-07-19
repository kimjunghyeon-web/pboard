package com.icia.pboard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icia.pboard.service.BoardService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class BoardServiceTest {
	@Autowired
	private BoardService service;
	
//	@Test
	public void Test() {
		assertThat(service.easyRead(21, "SPRING11").getReadCnt(), is(1));
	}
	
}
