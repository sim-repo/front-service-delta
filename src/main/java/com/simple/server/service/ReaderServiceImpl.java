package com.simple.server.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import com.simple.server.domain.contract.BusClassificator;
import com.simple.server.domain.contract.BusTagTemplate;
import com.simple.server.service.remote.IRemoteLogService;


@SuppressWarnings("unchecked")
@Service("readerService")
@Scope("singleton")
public class ReaderServiceImpl implements IReaderService{

	@Autowired
	private ApplicationContext ctx;

	private IRemoteLogService getRemoteLogServiceBean(){
		return (IRemoteLogService)ctx.getBean("remoteLogService");
	}
	
	@Override
	public List<BusTagTemplate> getAllTags() throws Exception{		
		BusTagTemplate instance = new BusTagTemplate();												
		return (List<BusTagTemplate>)(Object)getRemoteLogServiceBean().getAllMsg(instance);
	}

	@Override
	public List<BusClassificator> getAllClassificator() throws Exception{
		BusClassificator instance = new BusClassificator();												
		return (List<BusClassificator>)(Object)getRemoteLogServiceBean().getAllMsg(instance);
	}

	@Override
	public List<BusClassificator> getClassificatorBySqlCriteria(String sql) throws Exception {
		BusClassificator instance = new BusClassificator();		
		return (List<BusClassificator>)(Object)getRemoteLogServiceBean().getMsgBySqlCriteria(instance, sql);
	}	
		
}
