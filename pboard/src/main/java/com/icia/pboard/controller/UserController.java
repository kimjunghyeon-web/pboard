package com.icia.pboard.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.pboard.dto.UserDto;
import com.icia.pboard.exception.PasswordCheckFailException;
import com.icia.pboard.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/user/join")
	public ModelAndView join() {
		return new ModelAndView("main").addObject("viewname", "user/join.jsp");
	}

	@GetMapping("/user/login")
	public ModelAndView login() {
		return new ModelAndView("main").addObject("viewname", "user/login.jsp");
	}

	@GetMapping("/user/join_check")
	public ModelAndView joinCheck(@RequestParam String checkCode) {
		service.joinCheck(checkCode);
		return new ModelAndView("redirect:/user/login");
	}

	@GetMapping("/user/find")
	public ModelAndView find() {
		return new ModelAndView("main").addObject("viewname", "user/find.jsp");
	}

	@PostMapping("/user/reset_pwd")
	public ModelAndView resetPwd(@ModelAttribute @Valid UserDto.ResetPassword dto, BindingResult bindingResult,
			RedirectAttributes ra) throws BindException {
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.resetPwd(dto);
		ra.addFlashAttribute("msg", "임시비밀번호가 이메일로 전송되었습니다.");
		return new ModelAndView("redirect:/user/login");
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/user/info")
	public ModelAndView info(Principal principal, HttpSession session) {
		if(session.getAttribute("passwordCheck")==null)
			throw new PasswordCheckFailException("비밀번호를 확인해 주세요");
		return new ModelAndView("main").addObject("viewname", "user/info.jsp").addObject("user", service.info(principal.getName()));

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/user/password_check")
	public ModelAndView passwordCheck() {
		return new ModelAndView("main").addObject("viewname", "user/password_check.jsp");
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/user/password_check")
	public ModelAndView passwordCheck(@RequestParam String password, Principal principal, HttpSession session) {
		service.passwordCheck(password, principal.getName());
		session.setAttribute("passwordCheck", true);
		return new ModelAndView("redirect:/user/info");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/user/resign")
	public ModelAndView resign(SecurityContextLogoutHandler handler, HttpServletRequest request, 
			HttpServletResponse response, Authentication authentication) {
		service.resign(authentication.getName());
//		handler.logout(request, response, authentication);
		return new ModelAndView("redirect:/");
	}
	
	
}
