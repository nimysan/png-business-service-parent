package com.vluee.png.shrfacade.domain.model;

/**
 * 
 * 
 * 
 * @author SeanYe
 *
 */
public interface VcodeRequestRepository {

	void store(VcodeRequest request);

	VcodeRequest get(String sessionIdentifier);

}
