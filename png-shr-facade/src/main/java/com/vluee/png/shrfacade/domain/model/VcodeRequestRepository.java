package com.vluee.png.shrfacade.domain.model;

/**
 * 
 * 
 * 
 * @author SeanYe
 *
 */
public interface VcodeRequestRepository {

	String get(String sessionIdentifier);

	void store(VcodeRequest request);

	VcodeRequest getLatestRequest(String sessionIdentifier);

}
