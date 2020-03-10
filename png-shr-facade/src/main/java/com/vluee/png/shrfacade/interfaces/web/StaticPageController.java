package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableMap;
import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.application.service.HrService;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.service.VcodeService;
import com.vluee.png.shrfacade.interfaces.web.assembler.SalaryVoAssembler;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StaticPageController {

	@Autowired
	private PngExceptionHandler exceptionHandler;

	@Autowired
	private HrService hrService;

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private SalaryVoAssembler assembler;

	@GetMapping("/salary")
	public String greeting(HttpSession session, @RequestParam(name = "mobile", required = true) String mobile,
			@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "vcode", required = true) String vcode, Model model) {
		try {
			authenticatedRequest(session, mobile, vcode);
			HrUser userByMobile = hrService.getUserByMobile(mobile, userName);
			EmployeeMonthSalary salary = hrService.fetchSalary(userByMobile.getUserId());
			if (salary == null) {
				exceptionHandler.throwExceptionWithCode(PngBusinessException.EC_HR_NO_SALARY_DATA);
			}
			model.addAttribute("salary", assembler.assemble(salary));
		} catch (PngBusinessException e) {
			model.addAllAttributes(ImmutableMap.of("errorcode", e.getErrorCode(), "message", e.getMessage()));
			log.error(String.format("Failed to get salary for mobile %s and vcode %s", mobile, vcode), e);
			return "getcode";
		} catch (Exception e) {
			model.addAllAttributes(ImmutableMap.of("errorcode", "PNG-ERROR-0001", "message", "系统未知错误"));
			log.error(String.format("Failed to get salary for mobile %s and vcode %s", mobile, vcode), e);
			return "getcode";
		}
		return "salary";
	}

	private void authenticatedRequest(HttpSession session, String mobile, String vcode) {
		vcodeService.validateVcode(session.getId(), mobile, vcode);
	}

	@GetMapping("/getcode")
	public String getCode() {
		return "getcode";
	}

}