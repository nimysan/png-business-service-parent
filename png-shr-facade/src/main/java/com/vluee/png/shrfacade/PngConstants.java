package com.vluee.png.shrfacade;

import java.util.concurrent.TimeUnit;

public final class PngConstants {

	public static final String SSO_EC_TOEKN_EXPIRED = "401";
	public static final String SSO_RETURN_CODE_OK = "200";
	public static final String SSO_NO_USER_CODE = "500";
	public static final String SSO_HEADER_PLATENO_SSO_AUTHENTICATE = "plateno-sso-authenticate";
	public static final String SSO_API_GET_USER_BY_MOBILE = "/cxf/hrManage/getUserInfoListByMobile";
	
	public static final String SHR_API_GET_SALARY = "getUserPayData";
	
	
	//同一个用户允许发送短信请求的时间间隔
	public static final long VCODE_REQUEST_LIVE_TIME = TimeUnit.MINUTES.toMillis(1);
	//短信验证码的有效期
	public static final long VCODE_LIVE_TIME = TimeUnit.MINUTES.toMillis(10);
	
	
	//用户信息缓存1个小时
	public static final long HR_USERS_CACHE_DURATION = TimeUnit.MINUTES.toMillis(60);
	//工资条信息缓存1个小时
	public static final long HR_SALARY_CACHE_DURATION = TimeUnit.MINUTES.toMillis(60);
}
