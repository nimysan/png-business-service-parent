package com.vluee.png.shrfacade.domain.model.hr;

public interface HrUserRepository {

	public HrUser getByMobile(String mobile);

	public void store(String mobile, HrUser hrUser);
}
