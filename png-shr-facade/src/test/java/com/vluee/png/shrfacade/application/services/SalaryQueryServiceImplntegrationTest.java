package com.vluee.png.shrfacade.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("integration-test")
@Slf4j
class SalaryQueryServiceImplntegrationTest {

	@Autowired
	private SalaryQueryService salaryQueryService;

	@Test
	@DisplayName("验证正常获取验证码并成功获取工资条")
	void vierfyGetSalary() {
		String sessionIdentifier = UUID.randomUUID().toString();
		String mobile = "13412341234";
		String userName = "haris";
		String vcode = salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile, userName);
		EmployeeMonthSalary salary = salaryQueryService.getSalary(sessionIdentifier, mobile, userName, vcode);
		log.info("------{}---------", salary);
		assertNotNull(salary);
		assertThat(salary.getYear()).isNotBlank();
	}

	@Test
	@DisplayName("验证-手机号是铂涛用户并获取验证码")
	void verifyGetVcode() {
		String sessionIdentifier = UUID.randomUUID().toString();
		String mobile = "13412341234";
		String userName = "haris";
		assertThat(salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile, userName)).isNotBlank();
	}

	@Test
	@DisplayName("验证-非铂涛用户手机号获取验证码")
	void verifyGetVcodeforNonPngUser() {
		String sessionIdentifier = UUID.randomUUID().toString();
		String mobile = "13412342222";
		String userName = "haris";
		assertThrows(PngBusinessException.class,
				() -> salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile, userName));
	}

	@Test
	@DisplayName("限定时间内多次尝试获取vcode")
	void verifyGetVcodeInDuration() {
		String sessionIdentifier = UUID.randomUUID().toString();
		String mobile = "13412341234";
		String userName = "haris";
		assertThat(salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile, userName)).isNotBlank();

		assertThrows(PngBusinessException.class,
				() -> salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile, userName),
				"can't get vcode in short duration");
	}

}
