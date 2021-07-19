package com.icia.pboard;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.icia.pboard.entity.Comment;
import com.icia.pboard.service.BoardRestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class BoardRestTest {
	@Autowired
	private BoardRestService service;
	
	@Transactional
	@Test
	public void writeCommentTest() {
		Comment comment = new Comment(0, 21, "SPRING22", "아오열받아", null, "SPRING22.jpg");
		List<Comment> list = service.writeComment(comment, "SPRING22");
		System.out.println(list.size());
	}
}
