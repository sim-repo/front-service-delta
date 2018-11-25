package com.simple.server.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.BusApiItemMsg;
import com.simple.server.domain.contract.BusClassificator;
import com.simple.server.domain.contract.BusFilterGroup;
import com.simple.server.domain.contract.BusReportItem;
import com.simple.server.domain.contract.BusReportMsg;
import com.simple.server.domain.contract.BusTagTemplate;
import com.simple.server.domain.contract.IContract;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.http.HttpImpl;
import com.simple.server.util.DateTimeConverter;
import com.simple.server.util.ObjectConverter;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


@Controller
public class SyncReadController {

	@Autowired
	private AppConfig appConfig;
	
	private static final Logger logger = LogManager.getLogger(SyncReadController.class);

	/**
	 * param sql - любая sql-инструкция в LOG для операций чтения
	 * 
	 * @return json
	 */
	/* uncomment if necessary
	 *  
	@RequestMapping(value = "/sync/get/json/log/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogAnyGet(@PathVariable("sql") String sql) {
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}*/
	

	/*
	@RequestMapping(value = "/sync/get/json/nav/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	private @ResponseBody String jsonNavAnyGet(@PathVariable("sql") String sql,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql,
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	*/
	
	/**
	 * @param sql
	 *            - любая sql-инструкция в NAV для операций чтения
	 * @return json
	 */
	/*
	@RequestMapping(value = "/sync/get/xml/nav/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	private @ResponseBody String xmlNavAnyGet(@PathVariable("sql") String sql,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatXml(sql,
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	*/
	
	private void logInput(String url, ResponseEntity<String> res) {
		String bodySubstring = "null";
		
		if (res != null && res.getBody() != null) {
			if (res.getBody().length() > 50 ) {
				bodySubstring = res.getBody().substring(0, 49);
			} else 
				if (res.getBody().length() > 1) {
					bodySubstring = res.getBody().substring(0, 1);				
				}
		}
		
		logger.debug(String.format("SyncCtrl %s,  %s, thread id: %s , body: %s", System.currentTimeMillis(), url,  Thread.currentThread().getId(), bodySubstring));
	}

	/**
	 * <p> * Источник данных BTX ЛК: проверка логина в Битриксе </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param login (строка) - логин NAV-код клиента, обязательный
	 * @return
	 */
	@RequestMapping(value = "/sync/get/json/btx/login", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonBpmLoginGet(@RequestParam(value = "login", required = true) String login) {

		String key = "/sync/get/json/btx/login";
		String params = String.format("?login=%s", login);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		if (res != null)
			return res.getBody();
		else
			return null;
	}
	
	
	/**
	 * <p> * Источник данных NAV: Исключения по аттрибутам маркетинга </p>
	 * <p> Обращение: EXEC web_GetAttrExclusions @CustNo = '%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custId (строка) - NAV-код клиента, обязательный
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 * @return
	 */
	
	@RequestMapping(value = "/sync/get/json/nav/attrExclusions", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavAttrExclusionsGet(@RequestParam(value = "custId", required = true) String custId, 
														 @RequestParam(value = "companyName", required = false) String companyName,														 
														 @RequestParam(value = "endpointId", required = false) String endpointId) {		
		StringBuilder sql = new StringBuilder(String.format("EXEC web_GetAttrExclusions @CustNo = '%s', @CompanyName = '%s'", custId, companyName));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	/**
	 * <p> * Источник данных NAV: проверка существования клиента </p>
	 * <p> Обращение: EXEC web_CheckClientNav @BitrixId = '%s', @CompanyName = '%s' </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custId (строка) - Bitrix-код клиента, обязательный
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 * @return
	 */
	@RequestMapping(value = "/sync/get/json/nav/checkBitrixClientInNav", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCheckBitrixClientInNavGet(@RequestParam(value = "custId", required = true) String custId,
														 @RequestParam(value = "companyName", required = false) String companyName,
														 @RequestParam(value = "endpointId", required = false) String endpointId) {		
		
		StringBuilder sql = new StringBuilder(String.format("EXEC web_CheckClientNav @BitrixId = '%s', @CompanyName = '%s'", custId, companyName));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> * Источник данных 1C: возвращает список компаний </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"CompanyID": "19c55247-0569-11e7-80d2-005056910141",
	 * 				  "CompanyName": "ООО \"Ависта\"",
	 * 				  "CompanyINN": "7705498966"}]
	 */
	@RequestMapping(value = "/sync/get/json/1c/company", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneCompanyGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/company";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	/**
	 * <p> * Источник данных 1C: возвращает список менеджеров по отделам </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"DepartmentID": "95c1b9c8-0599-11e8-8116-005056910141",
	 * 				  "DepartmentCode": "СМЗП-0019",
	 * 				  "DepartmentName": "Группа логистики",
	 * 				  "ParentDepartmentID": "8d50324a-0598-11e8-8116-005056910141",
	 * 				  "CompanyID": "34fdd607-6369-11e7-80f4-005056910141",
	 * 				  "ManagerPersonID": "f256c325-468f-11e7-80d5-005056910141",
	 * 				  "ManagerEmployeeID": "611865ae-4694-11e7-80d6-005056910141",
	 * 				  "ManagerName": "Вася Пупкин"}]
	 */
	@RequestMapping(value = "/sync/get/json/1c/departments", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneDepGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/departments";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	/**
	 * <p> * Источник данных 1C: возвращает список сотрудников </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"PersonID": "d77384a9-b4b3-11e3-8063-00265554afe6",
	 * 				  "PersonCode": "С0488",
	 * 				  "FullName": "Вася Пупкин",
	 * 				  "FirstName": "Вася",
	 * 				  "LastName": "Пупкин",
	 * 				  "MiddleName": "Пупкин",
	 * 				  "Birthday": "1987-08-08T00:00:00",
	 * 				  "TIN": "741709715485",
	 * 				  "InsuranceNumber": "157-330-546 59",
	 * 				  "Gender": "Мужской",
	 * 				  "UserAccount": "SIMPLE\\vpupkin",
	 * 				  "UserName": "",
	 * 				  "Email": "vpupkin@simple.ru"}]
	 */
	@RequestMapping(value = "/sync/get/json/1c/persons", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/persons";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	
	/**
	 * <p> * Источник данных 1C: возвращает список изменений по сотрудникам </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON 
	 */
	@RequestMapping(value = "/sync/get/json/1c/personsChanges", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsChangeGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/personsChanges";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}


	/**
	 * <p> * Источник данных 1C: возвращает список неиспользованных дней отпуска </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"Date": "2018-05-30T00:00:00",
	 * 				  "EmployeeID": "28652e0d-b4b4-11e3-8063-00265554afe6",
	 * 				  "EmployeeName": "Вася Пупкин",
	 * 				  "CountDaysOfUnusedLeave": 18.67,
	 * 				  "CountDaysOfBalance": 26}
	 */	
	@RequestMapping(value = "/sync/get/json/1c/countDaysOfUnusedLeaveNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneDaysNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/countDaysOfUnusedLeaveNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	
	
	/**
	 * <p> * Источник данных 1C: возвращает список сотрудников </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"PersonID": "d77384a9-b4b3-11e3-8063-00265554afe6",
	 * 				  "PersonCode": "С0488",
	 * 				  "FullName": "Пупкин Вася Васильевич",
	 * 				  "FirstName": "Вася",
	 * 				  "LastName": "Васильевич",
	 * 				  "MiddleName": "Пупкин",
	 * 				  "Birthday": "1987-08-08T00:00:00",
	 * 				  "EmployeeID": "28652e0d-b4b4-11e3-8063-00265554afe6",
	 * 				  "EmployeeCode": "КПЗП-00474",
	 * 				  "PositionID": "4b9c68e1-d15a-11e7-8563-d10ce4c2b9af",
	 * 				  "Position": "Специалист по документальному сопровождению отдела клиентского сервиса",
	 * 				  "DepartmentID": "04a80812-194c-11e4-a258-00265554afe6",
	 * 				  "DepartmentCode_": "00022",
	 * 				  "DepartmentName": "Отдел клиентского сервиса",
	 * 				  "ManagerID": "f87ea38d-d0c7-11dc-9388-005056c00002",
	 * 				  "ManagerName": "Соболева Светлана Евгеньевна (осн.)",
	 * 				  "HireDate": "2014-03-18T00:00:00",
	 * 				  "DismissalDate": "0001-01-01T00:00:00",
	 * 				  "EmploymentType": 0,
	 * 				  "UserAccount": "SIMPLE\\vpupkin",
	 * 				  "UserName": "",
	 * 				  "Email": "vpupkin@simple.ru",
	 * 				  "CompanyID": "3dde5999-9956-11e7-8103-005056910141",
	 * 				  "ReasonOfDismissal": ""}]
	 */	
	@RequestMapping(value = "/sync/get/json/1c/EmployeesNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	/**
	 * <p> * Источник данных 1C: возвращает список периодов отпусков </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"EmployeeID": "28652e0d-b4b4-11e3-8063-00265554afe6",
	 * 				 "EmployeeName": "Вася Пупкин",
	 * 				 "UserAccount": "SIMPLE\\vpupkin",
	 * 				 "Status": "",
	 * 				 "StatusName": "Отпуск основной",
	 * 				 "DateStart": "2018-06-01T00:00:00",
	 * 				 "DateEnd": "2018-06-09T00:00:00",
	 * 				 "PersonID": "d77384a9-b4b3-11e3-8063-00265554afe6"}]
	 */	
	@RequestMapping(value = "/sync/get/json/1c/EmployeeStates", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeeStatesGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/EmployeeStates";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	/**
	 * <p> * Источник данных 1C: возвращает список изменений по сотрудникам </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON
	 */	
	@RequestMapping(value = "/sync/get/json/1c/EmployeesChanges", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesChangesGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesChanges";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

		
	/**
	 * <p> * Источник данных 1C: возвращает список cотрудников </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"PersonID": "d77384a9-b4b3-11e3-8063-00265554afe6",
	 * 				  "PersonCode": "С0488",
	 * 				  "FullName": "Пупкин Вася Васильевич",
	 * 				  "FirstName": "Вася",
	 * 				  "LastName": "Пупкин",
	 * 				  "MiddleName": "Васильевич",
	 * 				  "Birthday": "1987-08-08T00:00:00",
	 * 				  "TIN": "741709715485",
	 * 				  "InsuranceNumber": "157-330-546 59",
	 * 				  "Gender": "Мужской",
	 * 				  "UserAccount": "SIMPLE\\vpupkin",
	 * 				  "UserName": "",
	 * 				  "Email": "vpupkin@simple.ru"}
	 */		
	@RequestMapping(value = "/sync/get/json/1c/PersonsNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/PersonsNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	
	/**
	 * <p> * Источник данных 1C: возвращает служебный график </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"EmployeeID": "7c4aded4-bfd2-11e3-8064-00265554afe6",
	 * 				  "TypeOfWork": "Пятидневка",
	 * 				  "StartDate": "2018-05-30T10:00:00",
	 * 				  "EndDate": "2018-05-30T19:00:00"}
	 */		
	@RequestMapping(value = "/sync/get/json/1c/EmployeesSchedule", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesScheduleGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesSchedule";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	
	/**
	 * <p> * Источник данных BPM: возвращает баннеры по клиенту </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	 * @return JSON [{"EmployeeID": "7c4aded4-bfd2-11e3-8064-00265554afe6",
	 * 				  "TypeOfWork": "Пятидневка",
	 * 				  "StartDate": "2018-05-30T10:00:00",
	 * 				  "EndDate": "2018-05-30T19:00:00"}
	 */		
	@RequestMapping(value = "/sync/get/json/bpm/getClientBanners", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmClientBannersGet(@RequestParam(value = "navClientId", required = true) String navClientId) {
		String key = "/sync/get/json/bpm/GetClientBanners";
		String params = String.format("?navClientId=%s", navClientId);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		return res;
	}
	
	
	

	/**
	 * <p> * Источник данных 1C: Запрос на получение графиков работы сотрудников на дату </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param параметров нет
	*/		
	@RequestMapping(value = "/sync/get/json/1c/EmployeesScheduleByDate", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonOneEmployeesScheduleByDateGet(@RequestParam(value = "date", required = true) String date) {
		String key = "/sync/get/json/1c/EmployeesScheduleByDate";
		String params = String.format("?date=%s", date);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		if (res != null)
			return res.getBody();
		else
			return null;
	}
		
		
		
		

	/**
	 * <p> * Источник данных BPM: возвращает интервалы доставки </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param navClientId - NAV-код клиента, пример:К55949, обязательно
	 * @param productCategory - категория продукта, пример: "Алкоголь", обязательно
	 * @param navisionDatabase - база NAV, пример: "СПБ", обязательно
	 * @return JSON [
	 * 				 {"date":"5\/31\/2018",
	 * 				  "intervals":[
	 * 						{"endTime":"18:00",
	 * 						"startTime":"9:00"}
	 * 				]}
	 * 				]
	 * 				
	 */			
	@RequestMapping(value = "/sync/get/json/deliveryInterval", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmDeliveryIntervalGet(
			@RequestParam(value = "navClientId", required = true) String custNo,
			@RequestParam(value = "productCategory", required = true) String productCategory,
			@RequestParam(value = "navisionDatabase", required = true) String navisionDatabase			
			) {
		String key = "/sync/get/json/bpm/deliveryInterval";
		String params = String.format("?navClientId=%s&productCategory=%s&navisionDatabase=%s", custNo, productCategory, navisionDatabase);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		return res;
	}


	
	/**
	 * <p> Источник данных NAV: список персональных цен и скидок </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custId - NAV-код клиента, пример:К55949, не обязательно
	 * @param productId - категория продукта, пример: "106628", не обязательно
	 * @param companyId - база NAV, пример: "СИМПЛ", не обязательно
	 * @param shipmentMethod - тип отгрузки, пример: "2", не обязательно
	 * @param contractId - договор, пример: "AGS11-11594", не обязательно
	 * @param quantity - кол-во, не обязательно
	 * @param orderDate - дата доставки, пример 20180530, не обязательно
	 * @param locationId - РЦ1, не обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 * @return JSON [{
	 * 					"Discount":35,
	 * 					"Price":690,
	 * 					"ActivityCode":"",
	 * 					"AllowDisc":1
	 * 				}]
	 * 				
	 */			
	@RequestMapping(value = "/sync/get/json/nav/personalPrices", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavPersonalPriceslGet(
			@RequestParam(value = "custId", required = false, defaultValue = "") String custId,
			@RequestParam(value = "productId", required = false, defaultValue = "") String productId,
			@RequestParam(value = "shipmentMethod", required = false, defaultValue = "") String shipmentMethod,
			@RequestParam(value = "managerId", required = false, defaultValue = "") String managerId,
			@RequestParam(value = "contractId", required = false, defaultValue = "") String contractId,
			@RequestParam(value = "quantity", required = false, defaultValue = "") String quantity,
			@RequestParam(value = "orderDate", required = false, defaultValue = "") String orderDate,
			@RequestParam(value = "dim2", required = false, defaultValue = "") String dim2,
			@RequestParam(value = "locationId", required = false, defaultValue = "") String locationId,		
			@RequestParam(value = "companyId", required = false) String companyId,	
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_GetPersonalActionPrice] ");

		if (custId != null) {
			sql.append("@AccountCode = '" + custId + "',");
		}
		if (productId != null) {
			sql.append("@ProductCode = '" + productId + "',");
		}
		if (shipmentMethod != null) {
			sql.append("@ShipmentType = '" + shipmentMethod + "',");
		}
		if (managerId != null) {
			sql.append("@ManagerCode = '" + managerId + "',");
		}
		if (contractId != null) {
			sql.append("@ContractCode = '" + contractId + "',");
		}
		if (quantity != null) {
			sql.append("@Quantity = '" + quantity + "',");
		}
		if (orderDate != null) {
			sql.append("@Orderdate = '" + DateTimeConverter.dateToSQLFormat(orderDate) + "',");
		}
		if (dim2 != null) {
			sql.append("@Dim2 = '" + dim2 + "',");
		}
		if (locationId != null) {
			sql.append("@locationCode = '" + locationId + "',");
		}
		if (companyId != null) {
			sql.append("@CompanyCode = '" + companyId + "',");
		}
		endpointId =   endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId);
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.substring(0, sql.length() - 1).toString(), endpointId );
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> ЛК: Источник данных BPM: матрица клиентов </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param navClientId - NAV-код клиента, пример:К55949, обязательно	
	 * @return JSON  				
	 */			
	@RequestMapping(value = "/sync/get/json/clientMatrix", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmDeliveryIntervalGet(
			@RequestParam(value = "navClientId", required = true) String navClientId) {

		String key = "/sync/get/json/bpm/clientMatrixes";
		String params = String.format("?navClientId=%s", navClientId);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		logInput(key+params, res);
		return res;
	}

	
	/**
	 * <p> ЛК: Источник данных BPM: рекомендации клиентов </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param navClientId - NAV-код клиента, пример:К55949, обязательно	
	 * @return JSON [{"articles":["105267","103518","109235","110674","109237","107355","105015"],
	 * 				  "code":"REC-311",
	 * 				  "dateFrom":"4\/9\/2018",
	 * 				  "dateTo":"6\/30\/2018",
	 * 				  "description":"Наслаждайтесь утонченными винами и получайте шанс выиграть...",
	 * 				  "detailPicture": "AAQSkZJRgABAQEASABIAAD\/2wBDA..."
	 * 				}]		 		
	 */	
	@RequestMapping(value = "/sync/get/json/clientRecommendations", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmClientRecommendationGet(
			@RequestParam(value = "navClientId", required = true) String navClientId) {

		String key = "/sync/get/json/bpm/clientRecommendations";
		
		if (Base64.isBase64(navClientId)) {	
			byte[] converted = Base64.decodeBase64(navClientId.getBytes());
			navClientId = new String(converted, StandardCharsets.UTF_8);
		}
		String params = String.format("?navClientId=%s", navClientId);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);		
		logInput(key+params, res);
		return res;
	}

	
	
	
	
	/**
	 * <p> ЛК: Источник данных BPM: менеджеры </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param нет	
	 * @return JSON [{
	 * 					"code":"ab9c68d6-18e2-4519-b22e-590c3382d463",
	 * 					"navId":"АЛЕШ",
	 * 					"photo":null
	 * 				},
	 * 				{
	 * 					"code":"104c5f16-fc66-48ce-926a-170f679c68bf",
	 * 					"navId":"БЫКОВА_КВ",
	 * 					"photo":null
	 * 				}]		 		
	 */	
	@RequestMapping(value = "/sync/get/json/managerCRM", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmManagerGet(HttpServletRequest request) {

		String key = "/sync/get/json/bpm/manager";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		logInput(key, res);
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: мотивация сотрудников </p>
	 * <p> Обращение: EXEC [dbo].[sp_GetMotivatonCard] @EmpCode = '%s', @OnDate = %s</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param emplId - NAV-код сотрудника, пример:К55949, обязательно
	 * @param date - фомирование на дату, обязательно	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/empl/motivation", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavMotivationCardGet(
			@RequestParam(value = "emplId", required = true) String emplId ,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder(
				String.format("EXEC [dbo].[sp_GetMotivatonCard] @EmpCode = '%s', @OnDate=%s", emplId, DateTimeConverter.dateToSQLFormat(date)));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	
	
	/**
	 * <p> ЛК: Источник данных NAV: разделенные строки заказа продажи, сгруппированные по категориям </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно	
	 * @return JSON 		 		
	 */	
	@RequestMapping(value = "/sync/post/json/nav/so/item_category", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String xmlNavItemCategoryGet(HttpServletRequest request, @RequestBody String xml,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		String charset = "Windows-1251";
		if (request.getHeader("Accept-Charset") != null)
			charset = request.getHeader("Accept-Charset");
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain;charset=" + charset);

		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_getSorderSplit] '%s'", xml));
		String res = null;
		try {

			res = appConfig.getRemoteService().getFlatXml(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	/**
	 * <p> Источник данных NAV: клиентский прайс </p>
	 * <p> Обращение: EXECUTE [dbo].[ESB_Get_CustomerPrices] @CustNo='%s', @CompanyName='%s'", custNo, companyName)</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custNo - NAV-код клиента, пример:К55949, обязательно
	 * @param companyName - база NAV, пример: "СИМПЛ", обязательно
	 * @param companyName 
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно	
	 * @return JSON  {
	 * 					"UnitPrice":790,
	 * 					"Discount":15,
	 * 					"ItemNo":"000002",
	 * 					"Custno":"К44233"
	 * 				 }		 		
	 */	
	@RequestMapping(value = "/sync/get/json/nav/item/custPrices", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustPriceGet(@RequestParam(value = "custNo", required = true) String custNo,
			@RequestParam(value = "companyName", required = true) String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		
		
		StringBuilder sql = new StringBuilder(String
				.format("EXECUTE [dbo].[ESB_Get_CustomerPrices] @CustNo='%s', @CompanyName='%s'", custNo, companyName));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return res;
	}
	
	/**
	 * <p> Источник данных NAV: интернет прайс </p>
	 * <p> Обращение: EXEC [dbo].[web_getInternetPrice] @ItemNo='%s', @CompanyName='%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param itemNo - 75675, обязательно
	 * @param companyName - база NAV, пример: "СИМПЛ", обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно	
	 * @return JSON  [
	 * 					{
	 * 						"Qty":12,
	 * 						"Result":6240
	 * 					}
	 * 				]	 		
	 */	
	@RequestMapping(value = "/sync/get/json/nav/item/iprice", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemInternetPrice(
			@RequestParam(value = "itemNo", required = true) String itemNo,
			@RequestParam(value = "companyName", required = true) String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		
		StringBuilder sql = new StringBuilder(String
				.format("EXEC [dbo].[web_getInternetPrice] @ItemNo='%s', @CompanyName='%s'", itemNo, companyName));
		String res = null;
		try {			
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	
	/**
	 * <p> Источник данных NAV: клиентский прайс </p>
	 * <p> Обращение: EXEC [dbo].[web_getCustomerPrice] @CustNo='%s', @PriceDate='%s', @ItemNo='%s', @CompanyName='%s'</p>
	 * ../front/sync/get/json/nav/customerPrice?itemNo=000486&companyName=%D0%A1%D0%98%D0%9C%D0%9F%D0%9B&custNo=%D0%9A20600&priceDate=02.11.2018
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custNo - NAV-код клиента, пример:К55949, обязательно
	 * @param priceDate - дата, обязательно
	 * @param itemNo - 75675, обязательно
	 * @param companyName - база NAV, пример: "СИМПЛ", обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/customerPrice", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustPriceGet2(
			@RequestParam(value = "custNo", required = true) String custNo,
			@RequestParam(value = "priceDate", required = true) String priceDate,
			@RequestParam(value = "itemNo", required = true) String itemNo,
			@RequestParam(value = "companyName", required = true) String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId
			) {
		try {
			
			System.out.println( System.currentTimeMillis());
			logger.debug(String.format("/sync/get/json/nav/item/custPrices 1 %s,  thread id: %s ", System.currentTimeMillis(), Thread.currentThread().getId()));
			StringBuilder sql = new StringBuilder(String.format(
					"EXEC [dbo].[web_getCustomerPrice] @CustNo='%s', @PriceDate='%s', @ItemNo='%s', @CompanyName='%s'",
					custNo, DateTimeConverter.dateToSQLFormat(priceDate), itemNo, companyName));
			
			
			String res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));		
			logger.debug(String.format("/sync/get/json/nav/item/custPrices 4 %s,  thread id: %s ", System.currentTimeMillis(), Thread.currentThread().getId()));
			System.out.println( System.currentTimeMillis());
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	

	/**
	 * <p> Источник данных NAV: клиент </p>
	 * <p> Обращение: EXECUTE [dbo].[esb_GetCustomerXML] @CustNo='%s', @CompanyName='%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custNo - NAV-код клиента, пример:К55949, обязательно
	 * @param companyName - база NAV, пример: "СИМПЛ", обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/customer", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustomerGet(
			@RequestParam(value = "custNo", required = true) String custNo,
			@RequestParam(value = "companyName", required = true) String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(String
				.format("EXECUTE [dbo].[esb_GetCustomerXML] @CustNo='%s', @CompanyName='%s'", custNo, companyName));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatXml(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}





	
	/**
	 * <p> Источник данных NAV: кредитный лимит клиента + овердрафт </p>
	 * <p> Обращение: EXEC [dbo].[web_getCreditLimit] '%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param customerNo - NAV-код клиента, пример:К55949, обязательно	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/credit/{customerNo}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCreditGet(@PathVariable(value = "customerNo") String customerNo,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_getCreditLimit] '%s'", customerNo));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	
	/**
	 * <p> Источник данных NAV: узнать что с заказом в очереди BitrixQueue </p>
	 * <p> Обращение: EXEC [dbo].[web_getStateSorderQueue] '%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param @_outerSorderId - внешний номер заказа, обязательно	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/so/state/{outerSorderId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavSoStateGet(
			@PathVariable("outerSorderId") String outerSorderId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_getStateSorderQueue] @_outerSorderId = '%s'", outerSorderId));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: клиентские операции </p>
	 * <p> Обращение: EXEC [dbo].[web_getCustOperations] @_custNo = '%s', @_shipmentType=%s</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param custNo - NAV-код клиента, пример:К55949, обязательно
	 * @param shipmentType - тип отгрузки, не обязательно, по-умолчанию "0"		
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/transactions", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustTransactionGet(
			@RequestParam(value = "custNo", required = true) String custNo,
			@RequestParam(value = "shipmentType", required = false, defaultValue = "0") String shipmentType,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(
				String.format("EXEC [dbo].[web_getCustOperations] @_custNo = '%s', @_shipmentType=%s", custNo, shipmentType));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/** 
	 * <p> Источник данных NAV: список товаров/унификаторов по коду АП из акцизной марки </p>
	 * <p> Обращение: EXEC [dbo].[web_GetAlcoCodes]  @Alcocode = '%s' </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param alcocode - NAV-код АП, не обязательно	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/item/codes", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavAlcocodeGet(
			@RequestParam(value = "alcocode", required = false) String alcocode,		
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		
		
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_GetAlcoCodes] ");

		if (alcocode != null) {
			sql.append("@Alcocode = '" + alcocode + "'");
		}
		
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	/**
	 * <p> Источник данных NAV: узнать что с заказом в очереди BitrixQueue </p>
	 * <p> Обращение: EXEC [dbo].[web_getStateSorderQueue] </p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param outerCustomerId - внешний-код клиента, не обязательно
	 * @param outerSorderId - внешний номер заказа, не обязательно
	 * @param outerUserID - внешний пользоваль UserID, не обязательно
	 * @param sorderNo - NAV-код заказа, не обязательно
	 * @param customerId - NAV-код клиента, пример:К55949, не обязательно
	 * @param salespersonId - NAV-код менеджера, не обязательно
	 * @param phoneNo - номер телефона, не обязательно
	 * @param email - email, не обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/so/queue/state", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavSoQueueGet(
			@RequestParam(value = "outerCustomerId", required = false) String outerCustomerId,
			@RequestParam(value = "outerSorderId", required = false) String outerSorderId,
			@RequestParam(value = "outerUserID", required = false) String outerUserID,
			@RequestParam(value = "sorderNo", required = false) String sorderNo,
			@RequestParam(value = "customerId", required = false) String customerId,
			@RequestParam(value = "salespersonId", required = false) String salespersonId,
			@RequestParam(value = "phoneNo", required = false) String phoneNo,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getStateSorderQueue]");

		if (outerCustomerId != null) {
			sql.append("@_outerCustomerId = '" + outerCustomerId + "',");
		}
		if (outerSorderId != null) {
			sql.append("@_outerSorderId = '" + outerSorderId + "',");
		}
		if (outerUserID != null) {
			sql.append("@_outerUserID = '" + outerUserID + "',");
		}
		if (sorderNo != null) {
			sql.append("@_sorderNo = '" + sorderNo + "',");
		}
		if (customerId != null) {
			sql.append("@_customerId = '" + customerId + "',");
		}
		if (salespersonId != null) {
			sql.append("@_salespersonId = '" + salespersonId + "',");
		}
		if (phoneNo != null) {
			sql.append("@_phoneNo = '" + phoneNo + "',");
		}
		if (email != null) {
			sql.append("@_email = '" + email + "',");
		}

		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.substring(0, sql.length() - 1),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	

	
	/**
	 * <p>  Источник данных NAV: узнать что с заказом в очереди BitrixQueue </p>
	 * <p> Обращение: EXEC [dbo].[web_getStateSorderQueueShort] @_outerSorderId = '%s'</p>
	 * @author Иванов И.
	 * @version 1.0	 
	 * @param outerSorderId - внешний номер заказа, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/so/queue/shortState", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustTransactionGet(
			@RequestParam(value = "outerSorderId", required = true) String outerSorderId,				
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		
		StringBuilder sql = new StringBuilder(
				String.format("EXEC [dbo].[web_getStateSorderQueueShort] @_outerSorderId = '%s'",outerSorderId));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	/**
	 * <p> Источник данных NAV: список сотрудников </p>
	 * <p> Обращение: EXEC [dbo].[web_getEmployee] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/employee", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavEmployeeGet(@RequestParam(value = "custId", required = false) String custId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getEmployee]");

		if (custId != null)
			sql.append("@_custId ='" + custId + "'");

		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> Источник данных NAV: адрес доставки </p>
	 * <p> Обращение: EXEC [dbo].[web_getShipAddress] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно				
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/ship_addr", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavShipAddressGet(@RequestParam(value = "custId", required = false) String custId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getShipAddress]");
		if (custId != null)
			sql.append("@_custId ='" + custId + "'");

		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: возвращает резервы по клиенту </p>
	 * <p> Обращение: EXEC [dbo].[web_GetItemsBlOrder] @CustomerNo = '%s', @CompanyName=%s</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param custNo - NAV-код клиента, пример:К55949, обязательно
	 * @param company - компания, пример "СИМПЛ"	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/blorder", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavShipAddressGet(
			@RequestParam(value = "custId", required = false) String custId,
			@RequestParam(value = "company", required = false) String company,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		
		if (Base64.isBase64(custId)) {	
			byte[] converted = Base64.decodeBase64(custId.getBytes());
			custId = new String(converted, StandardCharsets.UTF_8);
		}
		if (Base64.isBase64(company)) {	
			byte[] converted = Base64.decodeBase64(company.getBytes());
			company = new String(converted, StandardCharsets.UTF_8);
		}
		
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_GetItemsBlOrder] ");
		if (custId != null)
			sql.append("@CustomerNo ='" + custId + "',");
		if (company != null)
			sql.append("@CompanyName ='" + company + "',");
		
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.substring(0, sql.length() - 1).toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	/**
	 * <p> Источник данных NAV: список менеджеров </p>
	 * <p> Обращение: EXEC [dbo].[web_getSalesperson] %s</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param custId - NAV-код клиента, пример:К55949, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/manager", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavManagerGet(@RequestParam(value = "custId", required = false) String custId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder filter = new StringBuilder();
		if (custId != null)
			filter.append(String.format("@_custno = '%s'", custId));

		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_getSalesperson] %s", filter.toString()));
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: список клиентов, совершивших покупку </p>
	 * <p> Обращение: EXEC [dbo].[web_getICustomers]</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/invoiced", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getManagers(@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getICustomers]");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}



	/**
	 * <p> Источник данных NAV: товарный каталог </p>
	 * <p> Обращение: EXEC [dbo].[web_isimple_getItemCatalog] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/isimple/items_catalog", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getISimpleItems(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		String sql = "EXEC [dbo].[web_isimple_getItemCatalog]";
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> Источник данных NAV: история клиентских продаж </p>
	 * <p> Обращение: EXEC web_GetSalesHistory @_customer_id = %s </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param custId - NAV-код клиента, пример:К55949, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/cust/history/{custId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getSalesHistory(@PathVariable("custId") String custId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_GetSalesHistory @_customer_id = '" + custId + "'");
		String res = null;
		try {
			res = appConfig.getRemoteService().getComplexJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: ограничения выписки </p>
	 * <p> Обращение: EXECUTE [dbo].[web_IsItemForbiddenForCompany] @_companyName=%s, @_itemNo=%s, @_cfo= ,@_custNo=%s, @_userID=%s, @_orderPostingDate=%s </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param custId - NAV-код клиента, пример:К55949, обязательно
	 * @param companyName - код NAV-компании, обязательно
	 * @param itemNo - NAV-id товара, обязательно
	 * @param cfo - канал продаж, обязательно
	 * @param userID - пользователь, обязательно
	 * @param orderPostingDate - дата учета документа, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/item/restrict", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRestrictItemCompany(
			@RequestParam(value = "companyName", required = true) String companyName,
			@RequestParam(value = "itemNo", required = true) String itemNo,
			@RequestParam(value = "cfo", required = true) String cfo,
			@RequestParam(value = "custId", required = true) String custId,
			@RequestParam(value = "userID", required = true) String userID,
			@RequestParam(value = "orderPostingDate", required = true) String orderPostingDate,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		String res = null;
		try {

			StringBuilder sql = new StringBuilder("EXECUTE [dbo].[web_IsItemForbiddenForCompany] ");
			sql.append("@_companyName='" + companyName + "'");
			sql.append(",@_itemNo='" + itemNo + "'");
			sql.append(",@_cfo='" + cfo + "'");
			sql.append(",@_custNo='" + custId + "'");
			sql.append(",@_userID='" + userID + "'");
			sql.append(",@_orderPostingDate='" + DateTimeConverter.dateToSQLFormat(orderPostingDate) + "'");

			res = appConfig.getRemoteService().getComplexJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: товарный остаток </p>
	 * <p> Обращение: SELECT [dbo].[f_GetAvailInvQty](%s, %s, '%s', '%s') as [qty] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param companyName - код NAV-компании, обязательно
	 * @param itemNo - NAV-id товара, обязательно
	 * @param locationCode - код склада, обязательно
	 * @param shipmentType - тип отгрузки, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/item/available", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemAvailable(@RequestParam(value = "itemNo", required = true) String itemNo,
			@RequestParam(value = "shipmentType", required = false, defaultValue = "2") String shipmentType,
			@RequestParam(value = "locationCode", required = false, defaultValue = "РЦ1") String locationCode,
			@RequestParam(value = "companyName", required = false, defaultValue = "СИМПЛ") String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		String res = null;
		try {
			StringBuilder json = new StringBuilder(
					String.format("SELECT [dbo].[f_GetAvailInvQty](%s, %s, '%s', '%s') as [qty]", itemNo, shipmentType,
							locationCode, companyName));

			res = appConfig.getRemoteService().getFlatJsonFirstObj(json.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> Источник данных NAV: товарный остаток по компании </p>
	 * <p> Обращение: EXEC [dbo].[web_GetInvQty] %s </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param companyName - код NAV-компании, обязательно
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/items/available/{companyName}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemAvailable(@PathVariable("companyName") String companyName,
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		String res = null;
		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_GetInvQty] %s", companyName));
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> Источник данных NAV: все товарные остатки</p>
	 * <p> Обращение: EXEC [dbo].[web_getAllInvQty] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/items/rem", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemsRemaining(
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		String res = null;
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getAllInvQty]");
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	/**
	 * <p> Источник данных NAV: создает учетки новых сотрудников компании</p>
	 * <p> Обращение: EXEC [dbo].[web_getDiscGroupsByEmpl] </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/employee/list", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getEmployeeList( @RequestParam(value = "endpointId", required = false) String endpointId) {

		String res = null;
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_getDiscGroupsByEmpl]");
		try {								
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}



	/**
	 * <p> Источник данных NAV: хэнбук цвета</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 0 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/color", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getColor(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 0");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук тип упаковки</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 1 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/pack_type", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getPackType(@RequestParam(value = "endpointId", required = false) String endpointId) {

		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 1");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук сорт вина</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 2 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/wine_type", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getWineType(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 2");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	/**
	 * <p> Источник данных NAV: хэнбук сорт вина</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 12 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/grape_kind", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getGrapeKind(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 12");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук винтаж рейтинг</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 13 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/rate_vintage", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRateVintage(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 13");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук тип вина</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 14 </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/type_wine", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getTypeWine(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 14");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук апелласьон</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 15</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/appelason", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getAppelason(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 15");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук стиль</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 16</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/style_type", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getStyleType(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 16");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук деканация</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 17</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/decantation", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getDecantation(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 17");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук рейтинг</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 18</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/rate_agency", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRaitingAgency(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 18");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук сахар</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 19</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/sugar_type", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getSugarType(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 19");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук регион</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 20</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/region", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRegion(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 20");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук производство</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 21</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/manufacture", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getManufacture(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 21");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук возраст</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 22</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/aging", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getAging(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 22");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук класс</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 23</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/class", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getClass2(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 23");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук raw</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 24</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/raw", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRaw(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 24");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук стиль</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 25</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/style", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getStyle(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 25");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук категория</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 26</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/drink_category", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getDrinkCategory(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 26");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	

	/**
	 * <p> Источник данных NAV: хэнбук еще одна категория</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 27</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/category", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getCategory(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 27");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук еще одна серия</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 30</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/series", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getSeries(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 30");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук способ производства</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 31</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/production_method", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getProductionMethod(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 31");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: хэнбук материал</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 31</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/material", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getMaterial(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 32");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук рекоммендации</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 33</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */	
	@RequestMapping(value = "/sync/get/json/nav/handbook/recommend_drink", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getRecommendDrink(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 33");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: хэнбук культивация</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 34</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/handbook/cultivation_type", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getCultivationType(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 34");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук стекло</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 34</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/handbook/accessory_class", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getAccessoryClass(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 37");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: хэнбук страна</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 34</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/handbook/country", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getCounty(@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 50");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	
	/**
	 * <p> Источник данных NAV: хэнбук товарный рейтинг</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 34</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/handbook/item_char_rating", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemCharacteristicRating2(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 51");
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}


	/**
	 * <p> Источник данных NAV: хэнбук виноград</p>
	 * <p> Обращение: EXEC web_getHandBook @_type = 34</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/handbook/item_char_grape", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemCharacteristicGrape(
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder("EXEC web_getHandBook @_type = 52");
		String res = null;
		try {			
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}
	
	
	/**
	 * <p> Источник данных NAV: ДоксИнБокс (DXBX) инвойсы</p>
	 * <p> POST-запрос, принимает XML</p>
	 * <p> в заголовках запроса использует клиентский Accept-Charset для возврата данных
	 * <p> Проверяет соединение через EXEC web_dxbx_getConnectionUrl @_type=2
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/dxbx/invoice", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> jsonNavDxbxGet(HttpServletRequest request, @RequestBody String xml,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		xml = xml.replaceFirst("<root>", "").replaceFirst("</root>", "");
		String json = null;
		String res = null;
		ResponseEntity<String> ret = null;
		String charset = "Windows-1251";
		if (request.getHeader("Accept-Charset") != null)
			charset = request.getHeader("Accept-Charset");
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain;charset=" + charset);
		try {

			json = ObjectConverter.xmlToJson(xml);

			String url = null;
			List<Map<String, Object>> list = appConfig.getRemoteService()
					.getListMap("EXEC web_dxbx_getConnectionUrl @_type=2");
			for (Map<String, Object> map : list) {
				for (Map.Entry<String, Object> pair : map.entrySet()) {
					url = (String) pair.getValue();
					break;
				}
			}
			if (url == null)
				throw new Exception("check proc web_dxbx_getConnection: url is null!");
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));

			headers.setContentType(mediaType);
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			ResponseEntity<String> r = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			res = ObjectConverter.jsonToXml(new String(r.getBody().getBytes("ISO-8859-1"), "UTF-8"), true);
			res = res.replaceFirst("UTF-8", "Windows-1251");
		} catch (HttpStatusCodeException e) {
			String errbody = null;
			try {
				errbody = ObjectConverter
						.jsonToXml(new String(e.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"), true);
				errbody = errbody.replaceFirst("UTF-8", "Windows-1251");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return new ResponseEntity<String>(errbody, responseHeaders, e.getStatusCode());
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(res, responseHeaders, HttpStatus.OK);
	}
	
	
	
	
	/**
	 * <p> Источник данных NAV: товарный каталог</p>
	 * <p> Обращение: EXEC [dbo].[web_sw_getItemCatalog] @ItemNo = %s </p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/item/catalog", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemCatalog(@RequestParam(value = "itemNo", required = false) String itemId,
											   @RequestParam(value = "endpointId", required = false) String endpointId) {	
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_sw_getItemCatalog] ");
		if(itemId != null)
			sql.append("@ItemNo ='"+itemId+"'");		
		
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));							
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;	
	}
	
	
	/**
	 * <p> Источник данных NAV: товарные рейтинги</p>
	 * <p> Обращение: EXEC [dbo].[web_sw_getItemRatings]</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param endpointId - инстанс СУБД: NAV, NAV_COPY, NAV_LK, не обязательно
	 */
	@RequestMapping(value = "/sync/get/json/nav/item/rating", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemRating(@RequestParam(value = "itemNo", required = false) String itemId,
											  @RequestParam(value = "endpointId", required = false) String endpointId) {	
		StringBuilder sql = new StringBuilder("EXEC [dbo].[web_sw_getItemRatings] ");
		if(itemId != null)
			sql.append("@ItemNo ='"+itemId+"'");		
		
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;	
	}
	
		
	/**
	 * <p> Источник данных NAV: данные для HR</p>
	 * <p> Обращение: EXEC [dbo].[sp_GetPlanFactHR] @Month = %s, @Year = %s,, @UpperCFUFilter = %s,, @CFUFilter = %s,, @SZFilter = %s,, @StartDate = %s,, @EndDate = %s,'</p>
	 * @author Иванов И.
	 * @version 1.0	 	
	 * @param month - месяц, не обязательно
	 * @param year - год, не обязательно
	 * @param upperCFUFilter - ЦФО, не обязательно
	 * @param cfuFilter - ЦФО, не обязательно
	 * @param szFilter - , не обязательно
	 * @param startDate - дата начала, не обязательно
	 * @param endDate - дата окончания, не обязательно
	 * 
	 */
	@RequestMapping(value = "/sync/get/json/nav/hr/expenditure", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String getItemRating(
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "upperCFUFilter", required = false) String upperCFUFilter,
			@RequestParam(value = "cfuFilter", required = false) String cfuFilter,
			@RequestParam(value = "szFilter", required = false) String szFilter,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "endpointId", required = false) String endpointId){	
		
		
		StringBuilder sql = new StringBuilder("EXEC [dbo].[sp_GetPlanFactHR] ");
		
		if(month != null)
			sql.append("@Month ='"+month+"',");		
		
		if(year != null)
			sql.append("@Year ='"+year+"',");	
		
		if(upperCFUFilter != null)
			sql.append("@UpperCFUFilter ='"+upperCFUFilter+"',");	
		
		if(cfuFilter != null)
			sql.append("@CFUFilter ='"+cfuFilter+"',");	
		
		if(szFilter != null)
			sql.append("@SZFilter ='"+szFilter+"',");	
		
		if(startDate != null)
			sql.append("@StartDate ='"+ DateTimeConverter.dateToSQLFormat(startDate) +"',");
		
		if(endDate != null)
			sql.append("@EndDate ='"+ DateTimeConverter.dateToSQLFormat(endDate) +"',");

		
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql.substring(0, sql.length() - 1).toString(),
					endpointId != null ? endpointId : appConfig.getDefaultEndpointByGroupId(appConfig.navGroupId));			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;	
	}
	
	
	
	

	@RequestMapping(value = "/tag", method = RequestMethod.GET)
	public String htmlTag(Locale locale, Model model) {
		model.addAttribute("serverTime", new Date());
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

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/classificator", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<BusClassificator> getClassificator() {
		try {
			return appConfig.getReaderService().getClassificatorBySqlCriteria("level like '1'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/agroup", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetDim() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site action group`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/actions", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetDimValue() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site actions`;";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/dim", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetDimRel() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site dimension`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/events", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetEvents() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site event`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/fgroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<IContract> jsonLogGetFgroup() {
		List<IContract> res = null;
		try {
			res = appConfig.getRemoteLogService().getAllMsg(new BusFilterGroup());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/tab", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetTab() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site tab`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/relations", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetRelations() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site filter relations`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/fdef", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetFdef() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site filter defaults`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/news", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetNews() {
		String res = null;
		String sql = "SELECT * FROM jdb.`bus news`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/reports", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BusTagTemplate> jsonGetReports() {
		try {
			return appConfig.getReaderService().getAllTags();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/post/json/log/reports", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String jsonRunReports(@RequestBody BusReportMsg req) {
		String result = null;
		try {
			String sqlTemplate = req.getSqlTemplate();
			StringBuilder sqlParam = new StringBuilder();
			List<BusReportItem> items = req.getItems();
			for (BusReportItem i : items) {
				sqlParam.append(" " + i.getParam() + "=" + i.getVal() + ",");
			}
			if (sqlParam.length() > 0)
				sqlParam.deleteCharAt(sqlParam.length() - 1);
			result = appConfig.getRemoteService().getFlatJson(sqlTemplate + " " + sqlParam.toString(),
					appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/admin", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetAdmin() {
		String res = null;
		String sql = "SELECT * FROM jdb.`site admin`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/api", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetApi() {
		String res = null;
		String sql = "SELECT * FROM jdb.`bus api`";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/post/json/log/api/item/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody StatusMsg jsonLogPostApiAdd(@RequestBody BusApiItemMsg msg) {
		String res = null;
		try {
			msg.setEndPointId(appConfig.LOG_ENDPOINT_NAME);
			msg.setIsDirectInsert(true);
			appConfig.getRemoteService().insert(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
		return appConfig.getSuccessStatus();
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/post/json/log/api/item/del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody StatusMsg jsonLogPostApiDel(@RequestBody BusApiItemMsg msg) {
		String res = null;
		try {
			msg.setEndPointId(appConfig.LOG_ENDPOINT_NAME);
			msg.setIsDirectInsert(true);
			appConfig.getRemoteService().delete(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
		return appConfig.getSuccessStatus();
	}

	@CrossOrigin(origins = "http://msk10websvc2:4200")
	@RequestMapping(value = "/sync/get/json/log/api/items", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogGetApiItem() {

		String res = null;
		String sql = "SELECT * FROM jdb.`bus api item` ORDER BY `top_priority` ASC";
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return res;
	}

	// @CrossOrigin(origins = "http://msk10websvc2:4200")
}
