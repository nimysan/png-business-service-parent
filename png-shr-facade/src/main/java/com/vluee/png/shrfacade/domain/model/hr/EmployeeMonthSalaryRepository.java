package com.vluee.png.shrfacade.domain.model.hr;

public interface EmployeeMonthSalaryRepository {

	EmployeeMonthSalary get(String userId);

	void store(String userId, EmployeeMonthSalary value);

}
