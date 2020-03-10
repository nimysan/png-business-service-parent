package com.vluee.png.shrfacade.domain.service.impl;

import static com.vluee.png.shrfacade.PngConstants.SSO_API_GET_USER_BY_MOBILE;
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
import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.model.hr.HrUserRepository;
import com.vluee.png.shrfacade.domain.service.PlateSSOService;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOClient;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("!not-in-office")
public class PlateSSOServiceImpl implements PlateSSOService {
	@Autowired
	private PngExceptionHandler exceptionHandler;

	@Autowired
	private HrUserRepository hrUserRepository;

	@Autowired
	private PlatenoSSOClient ssoClient;

	@Override
	public HrUser getUserByMobile(String mobile, String employeeName) {

		HrUser ifPresent = hrUserRepository.getByMobile(mobile);
		if (ifPresent != null) {
			return ifPresent;
		}
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("mobile", mobile);
		PlatenoSSOResponse ssoResponse = ssoClient.sendApiRequest(SSO_API_GET_USER_BY_MOBILE, params, HttpMethod.GET);
		log.debug("SSO fail for mobile {} with response {}", mobile, ssoResponse);
		if (!PngConstants.SSO_RETURN_CODE_OK.contentEquals(ssoResponse.getStatusCode())) {
			if (PngConstants.SSO_NO_USER_CODE.contentEquals(ssoResponse.getStatusCode())) {
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
				HrUser hrUser = hrUsers.get(0);
				hrUserRepository.store(mobile, hrUser);
				return hrUser;
			} else {
				exceptionHandler.throwExceptionWithCode(PngBusinessException.EC_HR_MULTI_USERS_WITH_SAME_PHONE);
			}
		}
		return null;

	}
}
