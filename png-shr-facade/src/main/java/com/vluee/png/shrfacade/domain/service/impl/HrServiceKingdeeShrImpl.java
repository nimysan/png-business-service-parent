package com.vluee.png.shrfacade.domain.service.impl;

import static com.vluee.png.shrfacade.PngConstants.API_GET_USER_BY_MOBILE;
import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_HR_CALL_ERROR;
import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_HR_NO_USER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vluee.png.shrfacade.PngConstants;
import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.model.assembler.EmployeeMonthSalaryAssembler;
import com.vluee.png.shrfacade.domain.service.HrService;
import com.vluee.png.shrfacade.infrastructure.shr.PngShrClient;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOClient;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 金蝶SHR服务提供了基础数据接口给到SSO，但是工资类的SSO尚未对接。 所以此处我们会从SSO拿用户信息，同时直连SHR获取工资数据信息。
 * 
 * @author SeanYe
 *
 */
@Service
@Profile("!integration-test")
@Slf4j
public class HrServiceKingdeeShrImpl implements HrService {

	@Autowired
	private PngExceptionHandler exceptionHandler;

	@Autowired
	private PlatenoSSOClient ssoClient;

	@Autowired
	private PngShrClient pngShrClient;

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {
		try {
			String ehrData = pngShrClient.getEhrData(userId, PngConstants.SHR_API_GET_SALARY, 1, 10);
			EmployeeMonthSalaryAssembler.assembleFromShrResponse(ehrData);
		} catch (Exception e) {
			log.error("Call shr failed ", e);
		}
		return null;
	}

	@Override
	public HrUser getUserByMobile(String mobile, String employeeName) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("mobile", mobile);
		PlatenoSSOResponse ssoResponse = ssoClient.sendApiRequest(API_GET_USER_BY_MOBILE, params, HttpMethod.GET);
		log.debug("SSO fail for mobile {} with response {}", mobile, ssoResponse);
		if (!PngConstants.RETURN_CODE_OK.contentEquals(ssoResponse.getStatusCode())) {
			if (PngConstants.NO_USER_CODE.contentEquals(ssoResponse.getStatusCode())) {
				exceptionHandler.throwExceptionWithCode(EC_HR_CALL_ERROR);
			} else {
				exceptionHandler.throwExceptionWithCode(EC_HR_NO_USER);
			}
		} else {
			List<HrUser> hrUsers = new ArrayList<HrUser>(2);
			JSONArray parseArray = JSONArray.parseArray(ssoResponse.getData());
			if (parseArray.size() == 0) {
				exceptionHandler.throwExceptionWithCode(EC_HR_NO_USER);
			}
			for (int i = 0; i < parseArray.size(); i++) {
				JSONObject jsonObject = parseArray.getJSONObject(i);
				String userId = jsonObject.getString("mebCardCode");
				String userName = jsonObject.getString("name");
				String userMobile = jsonObject.getString("mobile");
				if (mobile.contentEquals(userMobile) && employeeName.contentEquals(userName)) {
					hrUsers.add(new HrUser(userId, mobile, userName));
				}
			}
			if (hrUsers.size() == 1) {
				return hrUsers.get(0);
			} else {
				exceptionHandler.throwExceptionWithCode(PngBusinessException.EC_HR_MULTI_USERS_WITH_SAME_PHONE);
			}
		}
		return null;
	}
}
