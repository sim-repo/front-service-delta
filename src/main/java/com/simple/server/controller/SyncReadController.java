package com.simple.server.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.server.config.AppConfig;
import com.simple.server.config.EndpointType;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.domain.contract.Status;
import com.simple.server.domain.contract.BusReportMsg;
import com.simple.server.domain.contract.test.Book;
import com.simple.server.domain.contract.test.Dimension;
import com.simple.server.domain.contract.BusClassificator;
import com.simple.server.domain.contract.BusFilterGroup;
import com.simple.server.domain.contract.BusReportItem;
import com.simple.server.domain.contract.BusTagTemplate;
import com.simple.server.domain.contract.IContract;
import com.simple.server.service.remote.IRemoteLogService;
import com.simple.server.service.remote.IRemoteService;


@Controller
public class SyncReadController {

	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private AppConfig appConfig;
	
	
	
	@RequestMapping(value = "/sync/get/json/log/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogAnyGet(@PathVariable("sql") String sql){
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		try {
			res = remoteService.getFlatJson(sql, EndpointType.NAV);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	@RequestMapping(value = "/sync/get/json/nav/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonNavAnyGet(@PathVariable("sql") String sql){
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		try {
			res = remoteService.getFlatJson(sql, EndpointType.NAV);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	@RequestMapping(value = "/sync/get/xml/nav/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  xmlNavAnyGet(@PathVariable("sql") String sql){
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		try {
			res = remoteService.getFlatXml(sql, EndpointType.NAV);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@RequestMapping(value = "/sync/get/json/nav/so", method = RequestMethod.GET)
	public List<IContract> jsonNavSoGet(@RequestParam(value = "outerCustomerId", required = false) String outerCustomerId,
																	 @RequestParam(value = "outerSorderId", required = false) String outerSorderId,
																	 @RequestParam(value = "outerUserID", required = false) String outerUserID,
																	 @RequestParam(value = "sorderNo", required = false) String sorderNo,
																	 @RequestParam(value = "customerId", required = false) String customerId,
																	 @RequestParam(value = "salespersonId", required = false) String salespersonId,
																	 @RequestParam(value = "phoneNo", required = false) String phoneNo,
																	 @RequestParam(value = "email", required = false) String email,
																	 @RequestParam(value = "responseContractClass", required = false) String responseContractClass
																	){			
			List<IContract> res = null;
			IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
			
			SorderMsg so = new SorderMsg();
			so.setOuterSorderId(outerSorderId);
			so.setOuterCustomerId(outerCustomerId);
			so.setOuterUserID(outerUserID);
			so.setSorderNo(sorderNo);
			so.setCustomerId(customerId);
			so.setSalespersonId(salespersonId);
			so.setPhoneNo(phoneNo);
			so.setEmail(email);								
			so.setResponseContractClass(so.getClass().getName());
			
			try {			
				res = remoteService.getMsg(so);											
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
	}
	
	
	
	@RequestMapping(value = "/tag", method = RequestMethod.GET)	
	public String htmlTag(Locale locale, Model model) {	   
		model.addAttribute("serverTime", new Date() );		
		return "index";
	}
		
	
	@ModelAttribute("allTags")	
	public List<BusTagTemplate> getTags() {
		try {
			return appConfig.getReaderService().getAllTags();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	
	@ModelAttribute("allClassificator")		
	public List<BusClassificator> getAllClassificator() {
	    try {
			return appConfig.getReaderService().getAllClassificator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/classificator", method = RequestMethod.GET, produces="application/json")	
	public @ResponseBody List<BusClassificator> getClassificator() {
	    try {
			return appConfig.getReaderService().getClassificatorBySqlCriteria("level like '1'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}


	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/agroup", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetDim(){
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus action group`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/actions", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetDimValue(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus actions`;";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/dim", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetDimRel(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus dimension`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/events", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetEvents(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus event`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/fgroup", method = RequestMethod.GET)
	public @ResponseBody List<IContract>  jsonLogGetFgroup(){		
		IRemoteLogService remoteService = (IRemoteLogService)ctx.getBean("remoteLogService");
		List<IContract> res = null;
		try {
			res = remoteService.getAllMsg(new BusFilterGroup());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/tab", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetTab(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus tab`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/relations", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetRelations(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus filter relations`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/fdef", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetFdef(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus filter defaults`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/news", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetNews(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus news`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/reports", method = RequestMethod.GET)	
	public @ResponseBody List<BusTagTemplate> jsonGetReports() {			
		try {
			return appConfig.getReaderService().getAllTags();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/post/json/log/reports", method = RequestMethod.POST, produces="application/json;charset=UTF-8")	
	public @ResponseBody String jsonRunReports(@RequestBody BusReportMsg req) {			
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String result = null;
		try {			
			String sqlTemplate = req.getSqlTemplate();
			StringBuilder sqlParam = new StringBuilder();						
			List<BusReportItem> items = req.getItems();
			for(BusReportItem i: items){				
				sqlParam.append(" "+i.getParam()+"="+i.getVal()+",");				
			}
			if(sqlParam.length()>0)
				sqlParam.deleteCharAt(sqlParam.length()-1);							
			result = remoteService.getFlatJson(sqlTemplate+" "+sqlParam.toString(), EndpointType.NAV);								
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return result;			
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/sync/get/json/log/admin", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String  jsonLogGetAdmin(){		
		IRemoteService remoteService = (IRemoteService)ctx.getBean("remoteService");
		String res = null;
		String sql = "SELECT * FROM jdb.`bus admin`";
		try {
			res = remoteService.getFlatJson(sql, EndpointType.LOG);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	

}
