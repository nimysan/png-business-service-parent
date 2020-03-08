package com.vluee.png.shrfacade.domain.service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;

public interface HrService {

	public EmployeeMonthSalary fetchSalary(String userId);

	public HrUser getUserByMobile(String mobile);

}
