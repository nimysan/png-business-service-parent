package com.vluee.png.shrfacade.domain.service;

import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;

public interface ShrService {
	public EmployeeMonthSalary fetchSalary(String userId);
}
