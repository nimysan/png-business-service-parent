package com.vluee.png.shrfacade.domain.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.vluee.png.shrfacade.domain.model.assembler.EmployeeMonthSalaryAssembler;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.service.ShrService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile({ "integration-test", "not-in-office", "!prod" })
public class ShrServiceStub implements ShrService {

	Map<String, EmployeeMonthSalary> testData = new HashMap<>();

	public ShrServiceStub() {
		log.info("初始化一個測試ShrServiceStub");
		EmployeeMonthSalary sample = sample();
		testData.put("u_13412340000", sample);

		EmployeeMonthSalary sample1 = sample();
		testData.put("u_13412340001", sample1);

	}

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		return testData.get(userId);
	}

	private EmployeeMonthSalary sample() {
		try {
			return EmployeeMonthSalaryAssembler.assembleFromShrResponse(Joiner.on("")
					.join(IOUtils.readLines(ShrServiceStub.class.getClassLoader().getResourceAsStream("sample.json"),
							Charset.forName("utf8"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
