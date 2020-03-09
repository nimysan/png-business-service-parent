package com.vluee.png.shrfacade.infrastructure.shr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;

/**
 *           金蝶SHR服务提供了基础数据接口给到SSO，但是工资类的SSO尚未对接。
 *            所以此处我们会从SSO拿用户信息，同时直连SHR获取工资数据信息。
 * 
 * @author SeanYe
 *
 */
@Service
public class HrServiceKingdeeShrImpl implements HrService {

	@Autowired
	private PlatenoSSOClient ssoClient;

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		return null;
	}

	@Override
	public HrUser getUserByMobile(String mobile) {
		ssoClient.getUser(mobile);
		return null;
	}

}
