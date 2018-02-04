package com.simple.server.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.RedirectRouting;
import com.simple.server.util.ObjectConverter;


@Controller
public class SyncUtilController {
	
	@Autowired
	private AppConfig appConfig;
	
	
	@RequestMapping(value = "/cast/2xml", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = "text/plain;charset=Windows-1251")
	public ResponseEntity<String> toXML(HttpServletRequest request, @RequestBody String json) {				

		String charset = "utf-8";
		if(request.getHeader("Accept-Charset") != null)
			charset = request.getHeader("Accept-Charset");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","text/plain;charset="+charset);

		String res = null;
		try {											
			res = ObjectConverter.jsonToXml(json,true);						
		}
		catch(Exception e){			
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.BAD_REQUEST);								
		}
		return new ResponseEntity<String>(res, responseHeaders, HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "cast/2json", method = RequestMethod.POST,  consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> toJson(HttpServletRequest request, @RequestBody String xml) {				
		
		String charset = "utf-8";
		if(request.getHeader("Accept-Charset") != null)
			charset = request.getHeader("Accept-Charset");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","text/plain;charset="+charset);

		String res = null;
		try {											
			res = ObjectConverter.xmlToJson(xml);						
		}
		catch(Exception e){			
			e.printStackTrace();			
			return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.BAD_REQUEST);						
		}
		return new ResponseEntity<String>(res, responseHeaders, HttpStatus.OK);
	}	
	
	
	/**
	 * 
	 * @return route retranslate 
	 */
	@RequestMapping(value = "util/cache/allRetranslates", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonRetranslateGet() {		
		
		
		ConcurrentHashMap<String, RedirectRouting> map = new ConcurrentHashMap<String, RedirectRouting>();
		StringBuilder ret = new StringBuilder();
		
		for(Map.Entry<String, RedirectRouting> pair: appConfig.getRedirectRoutingsHashMap().entrySet()){
			RedirectRouting route = pair.getValue();
			
			ret.append(pair.getKey()+"---------------"+route.getUrl()+"\n\n\n");
		}
		
		return ret.toString();		
	}
	
	
	
	/**
	 * 
	 * @return route retranslate 
	 */
	@RequestMapping(value = "util/cache/check/retranslate", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonRetranslateGet(@RequestParam(value = "key", required = true) String key) {								
		return appConfig.getBusMsgService().checkRetranslate(key);			
	}
	
	

	/**
	 * 
	 * @return [jdb].[log pub success] 
	 */
	@RequestMapping(value = "util/log/success", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogSuccessGet(@RequestParam(value = "eventId", required = false) String eventId,
												  @RequestParam(value = "juuid", required = false) String juuid) {								

		StringBuilder sql = null;
		String res = null;
		if(eventId != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_success_byEventId`('%s');",eventId));
		else if (juuid != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_success_byUUID`('%s');",juuid));
			
		try {
			String original = appConfig.getRemoteService().getFlatJson(sql.toString(), appConfig.LOG_ENDPOINT_NAME);	
			res = ObjectConverter.prettyJson(original);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	
	/**
	 * 
	 * @return [jdb].[log pub err] 
	 */
	@RequestMapping(value = "util/log/err", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogErrGet(@RequestParam(value = "eventId", required = false) String eventId,
												  @RequestParam(value = "juuid", required = false) String juuid) {								

		StringBuilder sql = null;
		String res = null;
		if(eventId != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_err`('%s','');",eventId));
		else if (juuid != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_err`('','%s');",juuid));
			
		try {
			String original = appConfig.getRemoteService().getFlatJson(sql.toString(), appConfig.LOG_ENDPOINT_NAME);	
			res = ObjectConverter.prettyJson(original);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	
	
	/**
	 * 
	 * @return [jdb].[hot pub] 
	 */
	@RequestMapping(value = "util/log/hot", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogHotGet(@RequestParam(value = "eventId", required = false) String eventId,
												  @RequestParam(value = "juuid", required = false) String juuid) {								

		StringBuilder sql = null;
		String res = null;
		if(eventId != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_hotPub`('%s','');",eventId));
		else if (juuid != null)
			sql = new StringBuilder(String.format("CALL `jdb`.`get_log_hotPub`('','%s');",juuid));
			
		try {
			String original = appConfig.getRemoteService().getFlatJson(sql.toString(), appConfig.LOG_ENDPOINT_NAME);	
			res = ObjectConverter.prettyJson(original);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;			
	}
	
}
