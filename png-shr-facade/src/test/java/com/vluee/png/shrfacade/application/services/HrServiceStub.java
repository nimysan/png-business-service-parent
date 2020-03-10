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
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.model.assembler.EmployeeMonthSalaryAssembler;
import com.vluee.png.shrfacade.domain.service.HrService;

import lombok.extern.slf4j.Slf4j;

/**
 * 请不要在任何生产环境使用该类。
 * 
 * @author SeanYe
 *
 */
@Service
@Slf4j
@Profile("integration-test")
public class HrServiceStub implements HrService {

	private Map<String, String> userMap;
	private Map<String, EmployeeMonthSalary> salaryMap;

	@PostConstruct
	void data() {
		log.debug("-----初始化测试数据--------");
		userMap = ImmutableMap.of("13412341234", "user111", "13412341235", "user222", "15999651042", "user333");
		salaryMap = ImmutableMap.of("user111", createNewOne(), "user333", createNewOne());
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
		String userId = userMap.get(mobile);
		if (StringUtils.isNotBlank(userId))
			return new HrUser(userId, mobile, userName);

		return null;
	}

}
