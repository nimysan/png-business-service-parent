package com.vluee.png.shrfacade.domain.model;

public interface VcodeRepository {

	String get(String sessionIdentifier);

	void store(String sessionIdentifier, String mobile, String vcode);

}
