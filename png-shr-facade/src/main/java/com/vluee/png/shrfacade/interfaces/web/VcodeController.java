package com.vluee.png.shrfacade.interfaces.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vluee.png.shrfacade.application.services.VcodeService;

@RestController
public class VcodeController {

	@Autowired
	private VcodeService vcodeService;

	@PostMapping("/vcode")
	public ResponseEntity<BisResp> sendVcode(@RequestParam String mobile) {
		boolean ok = vcodeService.sendCode(mobile);
		if (ok) {
			return ResponseEntity.ok(BisResp.okEntity());
		} else {
			return ResponseEntity.ok(BisResp.okEntity());
		}
	}

}
