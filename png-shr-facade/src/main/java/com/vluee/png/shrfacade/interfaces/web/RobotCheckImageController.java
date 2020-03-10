package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RobotCheckImageController {

	@GetMapping("/robotCheckImage")
	public void getRobotVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
			response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0);
			ImageValidateCodeGenerator randomValidateCode = new ImageValidateCodeGenerator();
			randomValidateCode.generateImage(request, response);// 输出验证码图片方法
		} catch (Exception e) {
			log.error("获取图片验证码失败", e);
		}
	}

}
