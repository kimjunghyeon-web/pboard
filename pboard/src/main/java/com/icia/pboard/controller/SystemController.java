package com.icia.pboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SystemController {
	@GetMapping("/system/error")
	public ModelAndView error() {
		return new ModelAndView("main").addObject("viewname", "system/error.jsp");
	}
}
