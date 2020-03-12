package com.vluee.png.shrfacade.domain.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.service.PlateSSOService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile({ "integration-test", "not-in-office" })
public class PlateSSOServiceStub implements PlateSSOService {

	private Map<String, HrUser> userMap = new HashMap<String, HrUser>();

	public PlateSSOServiceStub() {
		log.info("初始化一個測試SSO STUB");
	}

	@PostConstruct
	void data() {
		log.debug("-----初始化HrUser测试数据--------");
		createUserWithMobileAndName("13412340000", "foo");
		createUserWithMobileAndName("13412340001", "bar");
		createUserWithMobileAndName("13412340002", "nosalaydata");
	}

	private void createUserWithMobileAndName(String mobile, String name) {
		HrUser hr = new HrUser("u_" + mobile, mobile, name);
		userMap.put(mobile, hr);
	}

	@Override
	public HrUser getUserByMobile(String mobile, String employeeName) {
		HrUser hrUser = userMap.get(mobile);
		if (hrUser == null) {
			return null;
		}
		if (!hrUser.getName().contentEquals(employeeName)) {
			return null;
		}
		return hrUser;
	}

}
