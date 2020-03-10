package com.vluee.png.shrfacade.domain.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;

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
	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		if (userId.contentEquals("u_15999651042")) {
			return createNewOne();
		}
		return null;
	}

	private EmployeeMonthSalary createNewOne() {
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
