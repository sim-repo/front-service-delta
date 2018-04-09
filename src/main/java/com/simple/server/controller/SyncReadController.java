package com.simple.server.controller;

import java.nio.charset.Charset;
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
import com.simple.server.util.DateTimeConverter;
import com.simple.server.util.ObjectConverter;

@Controller
public class SyncReadController {

	@Autowired
	private AppConfig appConfig;

	/**
	 * param sql - любая sql-инструкция в LOG для операций чтения
	 * 
	 * @return json
	 */
	@RequestMapping(value = "/sync/get/json/log/any/{sql:.+}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonLogAnyGet(@PathVariable("sql") String sql) {
		String res = null;
		try {
			res = appConfig.getRemoteService().getFlatJson(sql, appConfig.LOG_ENDPOINT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * <p>
	 * ИСТОЧНИК ДАННЫХ BTX
	 * </p>
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
	 * <p>
	 * ИСТОЧНИК ДАННЫХ 1C
	 * </p>
	 */

	@RequestMapping(value = "/sync/get/json/1c/company", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneCompanyGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/company";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/departments", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneDepGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/departments";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/persons", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/persons";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/personsChanges", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsChangeGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/personsChanges";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/countDaysOfUnusedLeave", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneDaysGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/countDaysOfUnusedLeave";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/countDaysOfUnusedLeaveNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneDaysNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/countDaysOfUnusedLeaveNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/EmployeeStates", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeeStatesGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/EmployeeStates";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/Employees", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesGet(HttpServletRequest request) {

		String key = "/sync/get/json/1c/Employees";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	@RequestMapping(value = "/sync/get/json/1c/EmployeesChanges", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesChangesGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesChanges";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	
				
	@RequestMapping(value = "/sync/get/json/1c/PersonsNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOnePersonsNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/PersonsNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	@RequestMapping(value = "/sync/get/json/1c/EmployeesNew", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesNewGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesNew";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	@RequestMapping(value = "/sync/get/json/1c/EmployeesSchedule", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonOneEmployeesScheduleGet(HttpServletRequest request) {
		String key = "/sync/get/json/1c/EmployeesSchedule";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}
	
	
	/**
	 * <p>
	 * ИСТОЧНИК ДАННЫХ BPM
	 * </p>
	 */

	/**
	 * <p>
	 * проект ЛК
	 * </p>
	 */

	/**
	 * 
	 * @return ЛК: интервалы доставки из BPM
	 */
	@RequestMapping(value = "/sync/get/json/deliveryInterval", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmDeliveryIntervalGet(
			@RequestParam(value = "navClientId", required = true) String custNo,
			@RequestParam(value = "productCategory", required = true) String productCategory) {
		String key = "/sync/get/json/bpm/deliveryInterval";
		String params = String.format("?navClientId=%s&productCategory=%s", custNo, productCategory);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		return res;
	}

	/**
	 * 
	 * @return ЛК: индивидуальные клиентские цены
	 */
	//
	@RequestMapping(value = "/sync/get/json/nav/personalPrices", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavPersonalPriceslGet(
			@RequestParam(value = "custId", required = false, defaultValue = "") String custNo,
			@RequestParam(value = "productId", required = false, defaultValue = "") String productId,
			@RequestParam(value = "shipmentMethod", required = false, defaultValue = "") String shipmentMethod,
			@RequestParam(value = "managerId", required = false, defaultValue = "") String managerId,
			@RequestParam(value = "contractId", required = false, defaultValue = "") String contractId,
			@RequestParam(value = "quantity", required = false, defaultValue = "") String quantity,
			@RequestParam(value = "orderDate", required = false, defaultValue = "") String orderDate,
			@RequestParam(value = "dim2", required = false, defaultValue = "") String dim2,
			@RequestParam(value = "locationId", required = false, defaultValue = "") String locationId,		
			@RequestParam(value = "companyId", required = false, defaultValue = "") String companyId,	
			@RequestParam(value = "endpointId", required = false) String endpointId) {

		
		StringBuilder sql = new StringBuilder(String.format(
				"EXEC [dbo].[web_GetPersonalActionPrice] " + "@AccountCode='%s', " + "@ProductCode='%s',"
						+ "@ShipmentType=%s," + "@ManagerCode='%s'," + "@ContractCode='%s'," + "@Quantity=%s,"
						+ "@Orderdate='%s'," + "@Dim2='%s'," + "@locationCode='%s'," + "@CompanyCode='%s'",
				custNo, productId, shipmentMethod, managerId, contractId, quantity, orderDate, dim2, locationId, companyId));
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
	 * 
	 * @return ЛК: матрица клиентов из BPM
	 */
	@RequestMapping(value = "/sync/get/json/clientMatrix", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmDeliveryIntervalGe(
			@RequestParam(value = "navClientId", required = true) String navClientId) {

		String key = "/sync/get/json/bpm/clientMatrixes";
		String params = String.format("?navClientId=%s", navClientId);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		return res;
	}

	/**
	 * 
	 * @return ЛК: рекомендации клиентов из BPM
	 */
	@RequestMapping(value = "/sync/get/json/clientRecommendations", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmClientRecommendationGet(
			@RequestParam(value = "navClientId", required = true) String navClientId) {

		String key = "/sync/get/json/bpm/clientRecommendations";
		String params = String.format("?navClientId=%s", navClientId);
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, params);
		return res;
	}

	/**
	 * 
	 * @return ЛК: менеджеры из BPM
	 */
	@RequestMapping(value = "/sync/get/json/managerCRM", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> jsonBpmManagerGet(HttpServletRequest request) {

		String key = "/sync/get/json/bpm/manager";
		ResponseEntity<String> res = appConfig.getBusMsgService().retranslate(key, "");
		return res;
	}

	/**
	 * @param customerNo
	 *            - NAV-код клиента
	 * @return ЛК: разделенные строки заказа продажи, сгруппированные по категориям
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
	 * @param customerNo
	 *            - NAV-код клиента
	 * @return ЛК:клиентский прайс
	 */
	@RequestMapping(value = "/sync/get/json/nav/custPrices", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
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
	 * @param customerNo
	 *            - NAV-код клиента
	 * @return ЛК:карточка клиента
	 */
	@RequestMapping(value = "/sync/get/json/nav/customer", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustomerGet(@RequestParam(value = "custNo", required = true) String custNo,
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
	 * <p>
	 * ИСТОЧНИК ДАННЫХ Navision
	 * </p>
	 */

	/**
	 * @param sql
	 *            - любая sql-инструкция в NAV для операций чтения
	 * @return json
	 */
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

	/**
	 * @param sql
	 *            - любая sql-инструкция в NAV для операций чтения
	 * @return json
	 */
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

	/**
	 * @param customerNo
	 *            - NAV-код клиента
	 * @return кредитный лимит клиента + овердрафт
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
	 * @param sorderId
	 *            - внутренний NAV-id заказа-продажи
	 * @return статус сообщения в очереди по заведению заказа-продажи в Нав
	 */
	@RequestMapping(value = "/sync/get/json/nav/so/state/{sorderId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavSoStateGet(@PathVariable("sorderId") String sorderId,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(String.format("EXEC [dbo].[web_getStateSorderQueue] '%s'", sorderId));
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
	 * @param custNo
	 *            - внутренний NAV-id клиента
	 * @param shipmentType
	 *            - тип отгрузки
	 * @return клиентские операции
	 */
	@RequestMapping(value = "/sync/get/json/nav/cust/transactions", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String jsonNavCustTransactionGet(
			@RequestParam(value = "custNo", required = true) String custNo,
			@RequestParam(value = "shipmentType", required = false) String shipmentType,
			@RequestParam(value = "endpointId", required = false) String endpointId) {
		StringBuilder sql = new StringBuilder(
				String.format("EXEC [dbo].[web_getCustOperations] @_custNo = '%s', @_shipmentType=%s",
						shipmentType == null ? 0 : shipmentType));
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
	 * @param custNo
	 *            - внутренний NAV-id клиента
	 * @param shipmentType
	 *            - тип отгрузки
	 * @return сообщения из очереди создания заказов
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
		sql.substring(0, sql.length() - 1);

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
	 * 
	 * @param custId
	 *            - внутренний NAV-id клиента
	 * @return список сотрудников
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
	 * 
	 * @param custId
	 *            - внутренний NAV-id клиента
	 * @return адреса доставки
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
	 * 
	 * @param custId
	 *            - внутренний NAV-id клиента
	 * @return список менеджеров
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
	 * 
	 * @return список клиентов, совершивших покупку
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
	 * 
	 * @return товарный каталог
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
	 * 
	 * @param custId
	 *            - внутренний NAV-id клиента
	 * @return история клиентски продаж
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
	 * 
	 * @param companyName
	 *            - код NAV-компании
	 * @param itemNo
	 *            - NAV-id товара
	 * @param cfo
	 *            - канал продаж
	 * @param custId
	 *            - NAV-id клиента
	 * @param userID
	 *            - пользователь
	 * @param orderPostingDate
	 *            - дата учета документа
	 * @return ограничения выписки
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
	 * 
	 * @param itemNo
	 *            - NAV-id товара
	 * @param shipmentType
	 *            - тип отгрузки
	 * @param locationCode
	 *            - код склада
	 * @param companyName
	 *            - код компании
	 * @return товарный остаток
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
	 * 
	 * @param companyName
	 *            - NAV-id компании
	 * @return товарный остаток по компании
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
	 * 
	 * @return весь товарный остаток
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
	 * 
	 * @return хэнбук цвета
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
	 * 
	 * @return хэнбук тип упаковки
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
	 * 
	 * @return хэнбук сорт вина
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
	 * 
	 * @return хэнбук сорт винограда
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
	 * 
	 * @return хэнбук винтаж рейтинг
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
	 * 
	 * @return хэнбук тип вина
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
	 * 
	 * @return хэнбук апелласьон
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
	 * 
	 * @return хэнбук стиль
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
	 * 
	 * @return хэнбук деканация
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
	 * 
	 * @return хэнбук рейтинг
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
	 * 
	 * @return хэнбук сахар
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
	 * 
	 * @return хэнбук регион
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
	 * 
	 * @return хэнбук производство
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
	 * 
	 * @return хэнбук возраст
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
	 * 
	 * @return хэнбук класс
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
	 * 
	 * @return хэнбук
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
	 * 
	 * @return хэнбук стиль
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
	 * 
	 * @return хэнбук категория
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
	 * 
	 * @return хэнбук еще одна категория
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
	 * 
	 * @return хэнбук серия
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
	 * 
	 * @return хэнбук способ производства
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
	 * 
	 * @return хэнбук материал
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
	 * 
	 * @return хэнбук рекоммендации
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
	 * 
	 * @return хэнбук культивация
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
	 * 
	 * @return хэнбук стекло
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
	 * 
	 * @return хэнбук страна
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
	 * 
	 * @return хэнбук товарный рейтинг
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
	 * 
	 * @return хэнбук виноград
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
	 * 
	 * @return товарный каталог
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
	 * 
	 * @return товарный рейтинг
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
	 * 
	 * @return expenditure
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
