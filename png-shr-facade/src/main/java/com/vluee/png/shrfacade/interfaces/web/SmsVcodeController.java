package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.application.service.HrService;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.service.VcodeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SmsVcodeController {

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private HrService hrService;

	@Autowired
	private PngExceptionHandler exceptionHandler;

	@GetMapping("/vcode")
	public ResponseEntity<BisResp> sendVcode(HttpSession session, @RequestParam String mobile,
			@RequestParam String userName, @RequestParam String robotCheckCode) {
		try {
			HrUser hrUser = hrService.getUserByMobile(mobile, userName);
			if (hrUser == null) {
				throw new Exception("找不到该手机号关联的铂涛员工数据或姓名与HR系统不匹配");
			}
			String attribute = (String) session.getAttribute(ImageValidateCodeGenerator.RANDOMCODEKEY);
			if (StringUtils.isNotBlank(attribute) && robotCheckCode.contentEquals(attribute)) {
				String sessionId = session.getId();
				vcodeService.sendCode(sessionId, mobile);
				return ResponseEntity.ok(BisResp.okEntity("验证码发送成功"));
			} else {
				throw new Exception("图形验证码错误，请重新输入");
			}
		} catch (Exception e) {
			log.error("測試", e);
			return ResponseEntity.ok(BisResp.fromException(exceptionHandler.output(e)));
		}
	}

}
