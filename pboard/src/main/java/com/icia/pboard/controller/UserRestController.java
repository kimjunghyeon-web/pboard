package com.icia.pboard.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icia.pboard.dto.UserDto;
import com.icia.pboard.entity.User;
import com.icia.pboard.service.UserRestService;

@RestController
public class UserRestController {
	@Autowired
	private UserRestService service;

	@GetMapping("/users/user/username")
	public ResponseEntity<?> idAvailableCheck(@RequestParam String username) {
		service.idAvailableCheck(username);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/users/user/email")
	public ResponseEntity<?> emailAvailableCheck(@RequestParam String email) {
		service.emailAvailableCheck(email);
		return ResponseEntity.ok(null);
	}

	@PostMapping("/users/new")
	public ResponseEntity<?> join(@ModelAttribute @Valid UserDto.Join dto, BindingResult bindingResult,
			@RequestParam MultipartFile profile) throws BindException {
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.join(dto, profile);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/users/username/email")
	public ResponseEntity<?> findId(@RequestParam String email) {
		String username = service.findId(email);
		return ResponseEntity.ok(username);
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/users/user")
	public ResponseEntity<?> update(UserDto.Update dto, Principal principal) {
		System.out.println(dto);
		return null;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/users/username")
	public ResponseEntity<?> changeIrum(@RequestParam String irum, Principal principal) {
		service.changeIrum(irum, principal.getName());
		return ResponseEntity.ok(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/users/password")
	public ResponseEntity<?> changePassword(@ModelAttribute @Valid UserDto.ChangePassword dto,
			BindingResult bindingResult ,Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.changePassword(dto, principal.getName());
		return ResponseEntity.ok(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/users/alter")
	public ResponseEntity<?> update(@ModelAttribute @Valid UserDto.Update dto,
			BindingResult bindingResult ,Principal principal, @RequestParam(required=false) MultipartFile profile) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.update(dto, profile, principal.getName());
		return ResponseEntity.ok(null);
	}
}
