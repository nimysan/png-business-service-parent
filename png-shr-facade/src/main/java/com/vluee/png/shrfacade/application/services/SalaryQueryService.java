package com.vluee.png.shrfacade.application.services;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;

public interface SalaryQueryService {

	/**
	 * 验证验证码是否是该用户之前发出的验证码，如果是，调用SHR接口获取工资条数据并返回。
	 * 
	 * @param mobile
	 */
	public EmployeeMonthSalary getSalary(String sessionIdentifier, String mobile, String userName, String vcode);

	/**
	 * 1. 验证手机号码是否是一个合理的铂涛员工，如果是 则发送验证给到客户
	 * 
	 * 2. 验证码10分钟内有效
	 * 
	 * 3. 一个session的用于1分钟内再次发送验证码
	 * 
	 * @param sessionIdentifier
	 * @param mobile
	 * @return
	 */
	public String sendVcodeToUser(String sessionIdentifier, String userName, String mobile);

}
