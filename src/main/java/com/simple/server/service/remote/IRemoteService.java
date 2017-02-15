package com.simple.server.service.remote;

import java.util.List;

import com.simple.server.config.AppConfig;
import com.simple.server.config.EndpointType;
import com.simple.server.domain.contract.IContract;

public interface IRemoteService {
	AppConfig getAppConfig() throws Exception;
	String getFlatJson(String sql, EndpointType endpoint) throws Exception;	
	String getFlatXml(String sql, EndpointType endpoint) throws Exception;	
	List<IContract> getMsg(IContract msg) throws Exception;
	List<IContract> getAllMsg(IContract msg) throws Exception;
	void insert(List<IContract> list) throws Exception;
}
