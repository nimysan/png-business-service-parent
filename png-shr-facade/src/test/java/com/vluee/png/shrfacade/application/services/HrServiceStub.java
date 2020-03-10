package com.vluee.png.shrfacade.application.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.vluee.png.shrfacade.application.service.HrService;
import com.vluee.png.shrfacade.domain.model.assembler.EmployeeMonthSalaryAssembler;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 请不要在任何生产环境使用该类。
 * 
 * @author SeanYe
 *
 */
@Service
@Slf4j
public class HrServiceStub implements HrService {

	private Map<String, HrUser> userMap;
	private Map<String, EmployeeMonthSalary> salaryMap;

	@PostConstruct
	void data() {
		log.debug("-----初始化测试数据--------");
		createUserWithMobileAndName("13412340000", "foo");
		createUserWithMobileAndName("13412340000", "bar");
		createUserWithMobileAndName("13412340000", "nosalaydata");
		salaryMap = ImmutableMap.of("user111", createNewOne(), "user333", createNewOne());
	}

	private void createUserWithMobileAndName(String mobile, String name) {
		HrUser hr = new HrUser("u_" + mobile, mobile, name);
		userMap.put(mobile, hr);
	}

	private EmployeeMonthSalary createNewOne() {
		try {
			return EmployeeMonthSalaryAssembler.assembleFromShrResponse(Joiner.on("").join(IOUtils.readLines(
					HrServiceStub.class.getClassLoader().getResourceAsStream("sample.json"), Charset.forName("utf8"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		return salaryMap.get(userId);
	}

	@Override
	public HrUser getUserByMobile(String mobile, String userName) {
		return userMap.get(mobile);
	}

}
