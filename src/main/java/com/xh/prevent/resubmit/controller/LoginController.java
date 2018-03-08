package com.xh.prevent.resubmit.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.xh.prevent.resubmit.commons.same.SameUrlData;

@Controller
public class LoginController {

	// 控制器本来就是单例，这样似乎更加合理
	private final Logger LOGGER = LogManager.getLogger(getClass());

	@GetMapping("/login")
	public String login() {
		LOGGER.info("GET请求登录");
		return "login";
	}

	@PostMapping("/login2")
	@SameUrlData
	public String login(HttpServletRequest request, String name, String password) {
		LOGGER.info("from请求如下：name=" + name + "\t password=" + password);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "redirect:/login";
	}

}
