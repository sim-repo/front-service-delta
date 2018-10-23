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
	
	
	/**
	 * <p> Утилита: узнать о наличии серверных ошибок, связанных с отправками сообщений </p>
	 * <p> Принцип: читаются логи на поиск парных записей – прием-отправка, если среди них есть только «прием», но нет отправки, то данные по этой записи вернутся</p>
	 * <p> Вызов http://msk10websvc2:8888/front/sync/get/json/log/server_err?eventId = СОБЫТИЕ</p>
	 * <p> пример 1: есть ли ошибки, связанные с отправкой заказов -  http://msk10websvc2:8888/front/sync/get/json/log/server_err?eventId=CREATE_SORDER</p>
	 * <p> пример 2: есть ли ошибки, связанные с отправкой договоров - http://msk10websvc2:8888/front/sync/get/json/log/server_err?eventId=ONE_NAV_AGREEMENT_BALANCE</p>
	 * <p> пример 3: есть ли ошибки, связанные с отправкой ЗНО - http://msk10websvc2:8888/front/sync/get/json/log/server_err?eventId=SHAREPNT_NAV_ZNO</p>
	 * @return возвращает список неотправленных сообщений
	 * @author Иванов И.
	 * @version 1.0	 	 
	 */
	@RequestMapping(value = "/sync/get/json/log/server_err", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetServer_err(@RequestParam(value = "eventId", required = false) String eventId) {

		String res = null;
		String where =  (eventId != null) ? " AND `event_id` LIKE '"+eventId +"'" : "";
		String sql = "CALL get_publog_serverErr()";

		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	/**
	 * <p> Утилита: преобразование JSON в XML через POST-запрос</p>
	 * @return возвращает XML</p>
	 * @author Иванов И.
	 * @version 1.0	 	 
	 */
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
	
	
	/**
	 * <p> Утилита: преобразование XML в JSON через POST-запрос</p>
	 * @return возвращает JSON</p>
	 * @author Иванов И.
	 * @version 1.0	 	 
	 */
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
	 * <p> Утилита: получить закэшированный список путей переадресаций </p>
	 * <p> Вызов: http://msk10websvc2:8888/front/util/cache/allRetranslates </p>
	 * <p> Используйте для проверки адресации, если возникли проблемы с получением данных  </p>
	 * <p> Внутренняя настроечная таблица: [router redirect] </p>
	 * @author Иванов И.
	 * @version 1.0	 	 
	 * @return [URL шины]---------------[URL клиентской системы, к которой происходит обращение]  	
	 *  <p>  /sync/get/json/bpm/clientMatrixes---------------https://crm.simple.ru/0/ServiceModel/TestDataService.svc/GetClientMatrixes</p>
	 * 	<p> здесь для получения данных по клиентским матрицам из CRM используется URL клиентской системы https://crm.simple.ru/0/ServiceModel/TestDataService.svc/GetClientMatrixes</p>
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
	 * <p> Утилита: получить закэшированный URL отдельной переадресации по ключу</p>
	 * <p> Вызов: http://msk10websvc2:8888/front/util/cache/check/retranslate?key=[Значение URL шины] </p>
	 * <p> Используйте для проверки отдельной адресации, если возникли проблемы с получением данных  </p>
	 * <p> Внутренняя настроечная таблица: [router redirect] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param key - текст, пример "/sync/get/json/bpm/manager" 
	 * @return [URL шины]---------------[URL клиентской системы, к которой происходит обращение]  ? key = [URL шины]
	 * 	<p> пример: узнать какой URL использует шина для получения данных по клиентским матрицам из CRM  </p>
	 *  <p>  http://msk10websvc2:8888/front/util/cache/check/retranslate?key=/sync/get/json/bpm/clientMatrixes</p>
	 */	 
	@RequestMapping(value = "util/cache/check/retranslate", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonRetranslateGet(@RequestParam(value = "key", required = true) String key) {								
		return appConfig.getBusMsgService().checkRetranslate(key);			
	}
	
	


	/**
	 * <p> Утилита: получение статуса было ли отправлено сообщение из шины </p>
	 *  происходит поиск записи по логу [jdb].[log pub success] 
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param eventId - текст, пример "NAV_AGREEMENT" 
	 * @param juuid - текст, пример "B36B560F-607C-41E3-BB83-3D9A2F5984F6" 
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
	 * <p> Утилита: получение статуса было ли ошибка по данному сообщению во время отправки из шины </p>
	 *  происходит поиск записи по логу [jdb].[log pub err] 
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param eventId - текст, пример "NAV_AGREEMENT" 
	 * @param juuid - текст, пример "B36B560F-607C-41E3-BB83-3D9A2F5984F6" 
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
	 * <p> Утилита: получение статуса поступило ли сообщение на бэкенд-шины </p>
	 *  происходит поиск записи по логу [jdb].[hot pub] 
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param eventId - текст, пример "NAV_AGREEMENT" 
	 * @param juuid - текст, пример "B36B560F-607C-41E3-BB83-3D9A2F5984F6" 
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
			if (appConfig.getRemoteService() == null)
				System.out.println("NNNNNUUUUULLL");
			String original = appConfig.getRemoteService().getFlatJson(sql.toString(), appConfig.LOG_ENDPOINT_NAME);	
			res = ObjectConverter.prettyJson(original);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;			
	}
	
}
