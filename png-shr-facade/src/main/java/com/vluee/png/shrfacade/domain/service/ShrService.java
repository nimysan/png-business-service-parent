package com.vluee.png.shrfacade.domain.service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;

public interface ShrService {

	public EmployeeMonthSalary fetchSalary(String phone);

	public void validateUserByMobile(String mobile);

}
