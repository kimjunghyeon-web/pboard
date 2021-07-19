package com.icia.pboard;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.icia.pboard.dao.BoardDao;
import com.icia.pboard.entity.Comment;
import com.icia.pboard.service.BoardRestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class BoardTest {
	@Autowired
	private BoardDao dao;
	@Autowired
	private BoardRestService service;
	
//	@Test
	public void findAllTest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startRowNum", 1);
		map.put("endRowNum", 500);
		System.out.println(dao.findAll(map).size());
	}

}
