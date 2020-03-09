package com.vluee.png.shrfacade.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;
import com.vluee.png.shrfacade.domain.service.VcodeService;

@ExtendWith(MockitoExtension.class)
class SalaryQueryServiceImplTest {

	@InjectMocks
	private SalaryQueryServiceImpl salaryQueryService;

	@Mock
	private VcodeService vcodeService;

	@Mock
	private HrService shrService;

	@Test
	void vierfyGetSalary() {
		String mobile = "1234";
		String sessionIdentifier = "123";
		String vcode = "123456";
		HrUser user = new HrUser("iamuser", mobile, "name");
		when(shrService.getUserByMobile(mobile)).thenReturn(user);
		when(shrService.fetchSalary(user.getUserId())).thenReturn(mock(EmployeeMonthSalary.class));
		salaryQueryService.getSalary(sessionIdentifier, mobile, vcode);
		verify(shrService, times(1)).getUserByMobile(mobile);
		verify(vcodeService, times(1)).validateVcode(sessionIdentifier, mobile, vcode);
	}

	@Test
	void vierfyGetSalaryWithWrongVcode() {
		String mobile = "1234";
		String sessionIdentifier = "123";
		String vcode = "123456";
		HrUser user = new HrUser("iamuser", mobile, "name");
		when(shrService.getUserByMobile(mobile)).thenReturn(user);
		doThrow(PngBusinessException.class).when(vcodeService).validateVcode(sessionIdentifier, mobile, vcode);
//		when(shrService.fetchSalary(user.getUserId())).thenReturn(mock(EmployeeMonthSalary.class));
		assertThrows(PngBusinessException.class, () -> {
			salaryQueryService.getSalary(sessionIdentifier, mobile, vcode);
		});
	}

	@Test
	void vierfyGetSalaryWithInvalidMobileUser() {
		String mobile = "1234";
		String sessionIdentifier = "123";
		String vcode = "123456";
		when(shrService.getUserByMobile(mobile)).thenReturn(null);
//		when(shrService.fetchSalary(user.getUserId())).thenReturn(mock(EmployeeMonthSalary.class));
		assertThrows(PngBusinessException.class, () -> {
			salaryQueryService.getSalary(sessionIdentifier, mobile, vcode);
		});
	}

	@Test
	void verifySendVcodeToUser() {
		String mobile = "1234";
		String sessionIdentifier = "123";
		when(vcodeService.sendCode(sessionIdentifier, mobile)).thenReturn("123456");
		when(shrService.getUserByMobile(mobile)).thenReturn(new HrUser("iamuser", mobile, "name"));
		assertThat(salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile)).isNotNull();
		verify(shrService, times(1)).getUserByMobile(mobile);
		verify(vcodeService).sendCode(sessionIdentifier, mobile);
	}

	@Test
	void verifySendCodeToInvalidUser() {
		String mobile = "1234";
		String sessionIdentifier = "123";
		when(shrService.getUserByMobile(mobile)).thenReturn(null);
//		doThrow(PngBusinessException.class).when(shrService).getUserByMobile(mobile);
		assertThrows(PngBusinessException.class, () -> {
			salaryQueryService.sendVcodeToUser(sessionIdentifier, mobile);
		});
	}

}
