package com.vluee.png.shrfacade.interfaces.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.vluee.png.shrfacade.domain.service.VcodeService;

@RestController
public class VcodeController {
	
   //TODO 需提供一个方法给到运维组同事根据手机号码获取验证码的功能
	
	@Autowired
	private VcodeService vcodeService;

	@PostMapping("/vcode")
	public ResponseEntity<BisResp> sendVcode(HttpServletRequest request, @RequestParam String mobile) {
		String sessionId = WebUtils.getSessionId(request);
		String vcode = vcodeService.sendCode(sessionId, mobile);
		if (StringUtils.isNotBlank(vcode)) {
			return ResponseEntity.ok(BisResp.okEntity());
		} else {
			return ResponseEntity.ok(BisResp.okEntity());
		}
	}

}
