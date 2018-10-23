package com.simple.server.service.remote;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.IContract;

public interface IRemoteService {
	AppConfig getAppConfig() throws Exception;
	String getFlatJson(String sql, String endpoint) throws DataAccessException, Exception;	
	String getComplexJson(String sql, String endpoint) throws Exception;
	String getFlatXml(String sql, String endpoint) throws Exception;	
	String getFlatJsonFirstObj(String sql, String endpoint) throws Exception;
	List<IContract> getMsg(IContract msg) throws Exception;
	List<IContract> getAllMsg(IContract msg) throws Exception;
	List<Map<String, Object>> getListMap(String sql) throws Exception;
	void insert(List<IContract> list) throws Exception;
	void insert(IContract msg) throws Exception;
	void insert(String sql, String endpoint) throws Exception;
	void delete(IContract msg) throws Exception;

}
