package com.icia.pboard.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icia.pboard.dao.AttachmentDao;
import com.icia.pboard.dto.CKResonse;
import com.icia.pboard.entity.Attachment;
import com.icia.pboard.entity.Comment;
import com.icia.pboard.service.BoardRestService;

import lombok.NonNull;

@RestController
public class BoardRestController {
	@Autowired
	private BoardRestService service;
	@Value("c:/upload/attachment")
	private String attachmentFolder;
	
	@GetMapping("/boards/image")
	public ResponseEntity<?> ckImageUpload(MultipartFile upload) throws IllegalStateException, IOException {
		CKResonse ckResponse = service.ckImageUpload(upload);
		return ResponseEntity.ok(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/comments")
	public ResponseEntity<?> writeComment(@ModelAttribute @Valid Comment comment, BindingResult bindingResult
			, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		return ResponseEntity.ok(service.writeComment(comment, principal.getName()));
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/comments")
	public ResponseEntity<?> deleteComment(@NonNull Integer cno, @NonNull Integer bno, Principal principal) {
		return ResponseEntity.ok(service.deleteComment(cno, bno, principal.getName()));
	}
	 
	@GetMapping("/attachments/{ano}")
	public ResponseEntity<?> viewAttachment(@PathVariable Integer ano) {
		Attachment attachment = service.findAttachmentId(ano);
		
		String originalFileName = attachment.getOriginalFileName();
		String saveFileName = attachment.getSaveFileName();
		File file = new File(attachmentFolder, saveFileName);
		HttpHeaders headers = new HttpHeaders();
		
		if(attachment.getIsImage()==true) {
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
			
			MediaType type = MediaType.IMAGE_JPEG;
			if(extension.toUpperCase().equals("PNG"))
				type = MediaType.IMAGE_PNG;
			else if(extension.toUpperCase().equals("GIF"))
				type = MediaType.IMAGE_GIF;
			
			headers.setContentType(type);
			headers.add("Content-Disposition", "inline;filename=" + originalFileName);
		} else {
			headers.add("Content-Disposition", "attachment;filename=" + originalFileName);
		}
		
		try {
			byte[] attachmentFile = Files.readAllBytes(file.toPath());
			return ResponseEntity.ok().headers(headers).body(attachmentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
			
	}
	
	@DeleteMapping("/attachments/{ano}")
	public ResponseEntity<?> deleteAttachment(@PathVariable Integer ano, @NonNull Integer bno, Principal principal) {
		return ResponseEntity.ok(service.deleteAttachments(ano,bno,principal.getName()));
	}
	
	
	
	
}
