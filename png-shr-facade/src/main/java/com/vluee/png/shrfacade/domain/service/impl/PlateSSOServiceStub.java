package com.vluee.png.shrfacade.domain.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.service.PlateSSOService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile({ "integration-test", "not-in-office", "!prod" })
public class PlateSSOServiceStub implements PlateSSOService {

	public PlateSSOServiceStub() {
		log.info("初始化一個測試SSO STUB");
	}

	@Override
	public HrUser getUserByMobile(String mobile, String employeeName) {
		if (mobile.contentEquals("15999651042")) {
			return new HrUser("u_" + mobile, mobile, "測試");
		}
		return null;
	}

}
