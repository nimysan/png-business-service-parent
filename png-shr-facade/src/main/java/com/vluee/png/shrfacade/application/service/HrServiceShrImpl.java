package com.vluee.png.shrfacade.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.service.PlateSSOService;
import com.vluee.png.shrfacade.domain.service.ShrService;

/**
 * 金蝶SHR服务提供了基础数据接口给到SSO，但是工资类的SSO尚未对接。 所以此处我们会从SSO拿用户信息，同时直连SHR获取工资数据信息。
 * 
 * @author SeanYe
 *
 */
@Service
public class HrServiceShrImpl implements HrService {

	@Autowired
	private PlateSSOService ssoService;

	@Autowired
	public ShrService shrService;


	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		return shrService.fetchSalary(userId);
	}

	@Override
	public HrUser getUserByMobile(String mobile, String employeeName) {
		return ssoService.getUserByMobile(mobile, employeeName);
	}
}
