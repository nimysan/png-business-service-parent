package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.application.services.SalaryQueryService;

@RestController
public class VcodeController {

	@Autowired
	private SalaryQueryService salaryQueryService;

	@Autowired
	private PngExceptionHandler exceptionHandler;

	@GetMapping("/vcode")
	public ResponseEntity<BisResp> sendVcode(HttpSession session, @RequestParam String mobile,
			@RequestParam String userName, @RequestParam String robotCheckCode) {
		try {
			String attribute = (String) session.getAttribute(ImageValidateCodeGenerator.RANDOMCODEKEY);
			if (StringUtils.isNotBlank(attribute) && robotCheckCode.contentEquals(attribute)) {
				String sessionId = session.getId();
				salaryQueryService.sendVcodeToUser(sessionId, mobile, userName);
				return ResponseEntity.ok(BisResp.okEntity("验证码发送成功"));
			} else {
				throw new Exception("请重新检查您的图形验证码");
			}
		} catch (Exception e) {
			return ResponseEntity.ok(BisResp.fromException(exceptionHandler.output(e)));
		}
	}

}
