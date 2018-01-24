package com.simple.server.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.simple.server.domain.contract.IContract;
import com.simple.server.domain.contract.RedirectRouting;
import com.simple.server.domain.contract.SessionFactory;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.mediators.Mediator;
import com.simple.server.mediators.Subscriber;
import com.simple.server.service.BusMsgService;
import com.simple.server.service.IReaderService;
import com.simple.server.service.LogBusMsgService;
import com.simple.server.service.ReaderServiceImpl;
import com.simple.server.service.remote.IRemoteLogService;
import com.simple.server.service.remote.IRemoteService;
import com.simple.server.statistics.PerfomancerStat;


@Service("appConfig")
@Scope("singleton")
public class AppConfig {	
	
	
	public final static String ACC = "jservice";
	public final static String PSW = "j123Service";
	public final static String DOMEN = "SIMPLE";
	public final static String WORKSTATION = "MSK10WEBSVC2";
	public final static String DATEFORMAT = "dd.MM.yyyy HH:mm:ss";		
	public final static String LOG_ENDPOINT_NAME = "LOG";
		
	
	private Subscriber subscriber = new Subscriber();
	
	private String serviceId;
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
    private MessageChannel channelBusBridge;
	
	@Autowired
    private MessageChannel channelBusLog;
	
	@Autowired
	private MessageChannel channelSrvLog;
	
	@Autowired
	private MessageChannel channelAdminLog;

	@Autowired
	private PerfomancerStat perfomancerStat;
	
	@Autowired
	private BusMsgService busMsgService;
	
	@Autowired
	private LogBusMsgService logBusMsgService;
	
	@Autowired
	private ReaderServiceImpl readerService;
	
	ConcurrentHashMap<String,RedirectRouting> redirectRoutingsHashMap = new ConcurrentHashMap<String, RedirectRouting>();	
	ConcurrentHashMap<String, String> sessionFactories = new ConcurrentHashMap<String, String>();
	
	
	private LinkedBlockingQueue<IContract> queueDirtyPlainText = new LinkedBlockingQueue<>(100);
    private LinkedBlockingQueue<IContract> queueDirtyMsg = new LinkedBlockingQueue<>(100);
    private LinkedBlockingQueue<IContract> queueClientMsg = new LinkedBlockingQueue<>(100);
    private LinkedBlockingQueue<IContract> queueAdminMsg = new LinkedBlockingQueue<>(10);
         
	private Mediator mediator = new Mediator();    	        
    private StatusMsg successStatus = new StatusMsg("202","Accepted");         
    
    public String navGroupId;
    
    public String getDefaultEndpointByGroupId(String endpointGroupId) {
		if(sessionFactories.containsKey(endpointGroupId))		
			return  sessionFactories.get(endpointGroupId);
		return null;
	}
    
	public void setSessionFactories(String endpointGroupId, String defaultEndpointId) {
		sessionFactories.put(endpointGroupId, defaultEndpointId);
	}

	public String getServiceId() {
		return serviceId;
	}

	public void initServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public ConcurrentHashMap<String, RedirectRouting> getRedirectRoutingsHashMap() {
		return redirectRoutingsHashMap;
	}
    
	public void setRedirectRoutingsHashMap(ConcurrentHashMap<String, RedirectRouting> redirectRoutingsHashMap) {
		this.redirectRoutingsHashMap = redirectRoutingsHashMap;
	}
	
	public void setRedirectRoutingHashMap(RedirectRouting routing){
		this.redirectRoutingsHashMap.put(routing.getMethodName(), routing);		
	}
	
	public ApplicationContext getApplicationContext() {
		return ctx;
	}
	
	public IRemoteService getRemoteService(){
		return (IRemoteService)ctx.getBean("remoteService");
	}
	
	public IRemoteLogService getRemoteLogService(){
		return (IRemoteLogService)ctx.getBean("remoteLogService");
	}
    
    public LinkedBlockingQueue<IContract> getQueueAdminMsg() {  
  		return queueAdminMsg;
  	}

	public LinkedBlockingQueue<IContract> getQueueDirtyPlainText() {
		return queueDirtyPlainText;
	}

	public LinkedBlockingQueue<IContract> getQueueDirtyMsg() {
		return queueDirtyMsg;
	}		

	public LinkedBlockingQueue<IContract> getQueueClientMsg() {
		return queueClientMsg;
	}

	public MessageChannel getChannelAdminLog() {
		return channelAdminLog;
	}
	
	public MessageChannel getChannelBusLog() {
		return channelBusLog;
	}

	public MessageChannel getChannelBusBridge() {
		return channelBusBridge;
	}

	public MessageChannel getChannelSrvLog() {
		return channelSrvLog;
	}	

	public Mediator getMediator() {
		return mediator;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public PerfomancerStat getPerfomancerStat() {
		return perfomancerStat;
	}

	public LogBusMsgService getLogBusMsgService() {
		return logBusMsgService;
	}

	public BusMsgService getBusMsgService() {
		return busMsgService;
	}		
	
	public IReaderService getReaderService() {
		return readerService;
	}

	public StatusMsg getSuccessStatus() {
		return successStatus;
	}

	public void initQueueDirtyMsg(int size){
		this.queueDirtyMsg = new LinkedBlockingQueue<>(size);
	}
	
	public void initQueueAdminMsg(int size){
		this.queueAdminMsg = new LinkedBlockingQueue<>(size);
	}
	
	public void initQueueDirtyPlainText(int size){
		this.queueDirtyPlainText = new LinkedBlockingQueue<>(size);
	}
	
	public void initQueueClientMsg(int size){
		this.queueClientMsg = new LinkedBlockingQueue<>(size);
	}
	
	public void initNavGroupId(String navGroupId) {
		this.navGroupId = navGroupId;
	}
			
}
