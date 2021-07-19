package com.icia.pboard.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icia.pboard.dao.AttachmentDao;
import com.icia.pboard.dao.CommentDao;
import com.icia.pboard.dao.UserDao;
import com.icia.pboard.dto.CKResonse;
import com.icia.pboard.dto.CommentDto;
import com.icia.pboard.entity.Attachment;
import com.icia.pboard.entity.Comment;

@Service
public class BoardRestService {
	@Value("c:/upload/image")
	private String imageFolder;
	@Value("http://localhost:8081/image/")
	private String imagePath;
	@Value("http://localhost:8081/profile/")
	private String profilePath;
	@Value("c:/upload/attachment")
	private String attachmentFolder;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private AttachmentDao attachmentDao;
	
	public CKResonse ckImageUpload(MultipartFile image) throws IllegalStateException, IOException {
		if(image!=null && image.isEmpty()==false) {
			if(image.getContentType().toLowerCase().startsWith("image/")) {
				String imageName = UUID.randomUUID().toString() + ".jpg";
				File file = new File(imageFolder, imageName);
				image.transferTo(file);
				return new CKResonse(1, imageName, imagePath + imageName);
			}
		}
		return null;
	}
	
	public List<Comment> writeComment(Comment comment, String username) {
		String profile = userDao.findById(username).getProfile();
		comment.setWriter(username);
		comment.setProfile(profilePath + profile);
		commentDao.insert(comment);
		return commentDao.findAllByBno(comment.getBno());
	}
	
	public List<Comment> deleteComment(Integer cno, Integer bno, String username) {
		commentDao.delete(cno);
		return commentDao.findAllByBno(bno);
	}

	public Attachment findAttachmentId(Integer ano) {
		return attachmentDao.findById(ano);
	}

	public List<Attachment> deleteAttachments(Integer ano, Integer bno, String name) {
		Attachment attachment = attachmentDao.findById(ano);
		attachmentDao.delete(ano);
		
		File file = new File(attachmentFolder, attachment.getOriginalFileName());
		if(file.exists())
			file.delete();
		return attachmentDao.findAllByBno(bno);
	}
}
