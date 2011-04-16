package com.mscg.test.server.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping(method=RequestMethod.GET)
	public String testAction(Model model) {
		model.addAttribute("testValue", "This is the test value");
		return "test";
	}

	@RequestMapping(value="/file", method=RequestMethod.GET)
	public String testFile(Model model) throws IOException {
		model.addAttribute("file-source",
			Thread.currentThread().getContextClassLoader().getResource("Varie.zip").openStream());
		model.addAttribute("file-size", 2364);
		model.addAttribute("content-type", "application/zip");
		model.addAttribute("file-name", "test.zip");
		return "fileResponse";
	}

}
