package com.vluee.png.shrfacade.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONObject;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOClient;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOResponse;

/**
 * 金蝶SHR服务提供了基础数据接口给到SSO，但是工资类的SSO尚未对接。 所以此处我们会从SSO拿用户信息，同时直连SHR获取工资数据信息。
 * 
 * @author SeanYe
 *
 */
@Service
@Profile("!integration-test")
public class HrServiceKingdeeShrImpl implements HrService {

	@Autowired
	private PlatenoSSOClient ssoClient;

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		return null;
	}

	@Override
	public HrUser getUserByMobile(String mobile) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("mobile", mobile);
		PlatenoSSOResponse ssoResponse = ssoClient.sendApiRequest("/cxf/hrManage/getUserInfoListByMobile", params);
		if ("500".contentEquals(ssoResponse.getStatusCode())) {
			return null;
		}
		JSONObject userJsonObj = JSONObject.parseObject(ssoResponse.getData());
		String userMobileFromHr = userJsonObj.getString("mobile");
		if (userMobileFromHr.contentEquals(mobile)) {
			return null;
		}
		HrUser hrUser = new HrUser(userJsonObj.getString("userId"), mobile, userJsonObj.getString("name"));
		return hrUser;
	}

}
