package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableMap;
import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.application.services.SalaryQueryService;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.interfaces.web.assembler.SalaryVoAssembler;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StaticPageController {

	@Autowired
	private SalaryQueryService salaryQueryService;

	@Autowired
	private SalaryVoAssembler assembler;

	@GetMapping("/salary")
	public String greeting(HttpSession session, @RequestParam(name = "mobile", required = true) String mobile,
			@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "vcode", required = true) String vcode, Model model) {
		try {
			EmployeeMonthSalary salary = salaryQueryService.getSalary(session.getId(), mobile, userName, vcode);
			if (salary != null) {
				model.addAttribute("salary", assembler.assemble(salary));
			}
		} catch (PngBusinessException e) {
			model.addAllAttributes(ImmutableMap.of("errorcode", e.getErrorCode(), "message", e.getMessage()));// TODO
			log.error(String.format("Failed to get salary for mobile %s and vcode %s", mobile, vcode), e);
			return "getcode";
		} catch (Exception e) {
			model.addAllAttributes(ImmutableMap.of("errorcode", "PNG-ERROR-0001", "message", "系统未知错误"));
			log.error(String.format("Failed to get salary for mobile %s and vcode %s", mobile, vcode), e);
			return "getcode";
		}
		return "salary";
	}

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
			e.printStackTrace();
		}
	}

	@GetMapping("/getcode")
	public String getCode() {
		return "getcode";
	}

}