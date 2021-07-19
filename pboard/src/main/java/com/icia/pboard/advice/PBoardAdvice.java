package com.icia.pboard.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.pboard.exception.JobMvcFailException;
import com.icia.pboard.exception.JobRestFailException;
import com.icia.pboard.exception.PasswordCheckFailException;

@ControllerAdvice
public class PBoardAdvice {
	
	@ExceptionHandler(JobMvcFailException.class)
	public ModelAndView jobMvcFailException(JobMvcFailException e, RedirectAttributes ra) {
		ra.addFlashAttribute("viewname", e.getMessage());
		return new ModelAndView("redirect:/system/error");
	}
	
	@ExceptionHandler(JobRestFailException.class)
	public ResponseEntity<?> jobRestFailExceptionHandler(JobRestFailException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	@ExceptionHandler(PasswordCheckFailException.class)
	public ModelAndView PCFEhandler(PasswordCheckFailException e, RedirectAttributes ra) {
		ra.addFlashAttribute("msg", e.getMessage());
		return new ModelAndView("redirect:/user/password_check");
	}
	
}
