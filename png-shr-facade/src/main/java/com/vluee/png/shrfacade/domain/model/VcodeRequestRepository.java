package com.vluee.png.shrfacade.domain.model;

public interface VcodeRequestRepository {

	public VcodeRequest get(String id);

	public void store(String sessionId, VcodeRequest request);
}
