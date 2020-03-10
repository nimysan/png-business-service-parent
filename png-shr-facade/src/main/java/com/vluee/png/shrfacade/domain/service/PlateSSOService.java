package com.vluee.png.shrfacade.domain.service;

import com.vluee.png.shrfacade.domain.model.hr.HrUser;

public interface PlateSSOService {
	public HrUser getUserByMobile(String mobile, String employeeName);
}
