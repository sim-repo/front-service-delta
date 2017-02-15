package com.simple.server.service.remote;

import java.util.List;

import com.simple.server.domain.contract.IContract;


public interface IRemoteLogService {
	List<IContract> getAllMsg(IContract msg) throws Exception;
	List<IContract> getMsgBySqlCriteria(IContract msg, String sql) throws Exception;
}
