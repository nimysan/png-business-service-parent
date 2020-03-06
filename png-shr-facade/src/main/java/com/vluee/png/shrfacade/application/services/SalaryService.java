package com.vluee.png.shrfacade.application.services;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;

public interface SalaryService {

	/**
	 * 验证验证码是否是该用户之前发出的验证码，如果是，调用SHR接口获取工资条数据并返回。
	 * 
	 * @param mobile
	 */
	public EmployeeMonthSalary getSalary(String mobile, String vcode)  throws Exception;

}
