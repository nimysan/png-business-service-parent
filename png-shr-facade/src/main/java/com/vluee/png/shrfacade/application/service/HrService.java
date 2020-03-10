package com.vluee.png.shrfacade.application.service;

import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;

public interface HrService {

	public EmployeeMonthSalary fetchSalary(String userId);

	public HrUser getUserByMobile(String mobile, String employeeName);

}
