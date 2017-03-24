package com.simple.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.config.EndpointType;
import com.simple.server.config.EventType;
import com.simple.server.config.OperationType;
import com.simple.server.domain.contract.BusPubMsg;
import com.simple.server.domain.contract.BusSubMsg;
import com.simple.server.domain.contract.BusWriteMsg;
import com.simple.server.domain.contract.ConfirmMsg;
import com.simple.server.domain.contract.ErrPubMsg;
import com.simple.server.domain.contract.ErrSubMsg;
import com.simple.server.domain.contract.IncomingBufferMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.domain.contract.SuccessPubMsg;
import com.simple.server.domain.contract.UniMinMsg;
import com.simple.server.domain.contract.UniMsg;
import com.simple.server.statistics.time.Timing;
import com.simple.server.util.ObjectConverter;


@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async")
public class AsyncPubController {

	@Autowired
	private AppConfig appConfig;

	@RequestMapping(value = "json/pub/uni", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonPub(@RequestBody UniMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			msg.setMethodHandler("/async/json/pub/uni");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusPubMsg.class);
			msg.setOperationType(OperationType.PUB);
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "xml/pub/uni", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public StatusMsg xmlPub(HttpServletRequest request, @RequestBody String xml) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			UniMsg msg = (UniMsg)ObjectConverter.xmlToObject(xml, UniMsg.class);			
			msg.setMethodHandler("/async/json/pub/uni");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusPubMsg.class);
			msg.setOperationType(OperationType.PUB);
			
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	

	@RequestMapping(value = "json/sub/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonConfirm(@RequestBody StatusMsg status) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			status.setMethodHandler("/async/json/sub/confirm");
			status.setChannel(appConfig.getChannelBusBridge());
			status.setLogClass(BusSubMsg.class);
			status.setOperationType(OperationType.SUB);
			status.setEventId(EventType.CONFIRM);
			
			appConfig.getQueueDirtyMsg().put(status);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "nav/pub/success", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNavPubSuccess(@RequestBody SuccessPubMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("nav pub success catch ::::: " + msg);
			msg.setMethodHandler("nav/pub/success");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.NAV);
			msg.setIsDirectInsert(true);
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "nav/pub/err", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNavPubErr(@RequestBody ErrPubMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("nav pub err catch ::::: " + msg);
			msg.setMethodHandler("nav/pub/err");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.NAV);
			msg.setIsDirectInsert(true);
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "nav/pub/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonConfirm(@RequestBody ConfirmMsg confirm) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("nav pub confirmation ::::: " + confirm);
			confirm.setMethodHandler("/async/json/sub/confirm");
			confirm.setChannel(appConfig.getChannelBusBridge());
			confirm.setLogClass(BusSubMsg.class);
			confirm.setOperationType(OperationType.WRITE);
			confirm.setEndPointId(EndpointType.NAV);
			confirm.setIsDirectInsert(true);
			appConfig.getQueueDirtyMsg().put(confirm);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "oktell/uni/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonOktellListener(@RequestBody UniMinMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("oktell listener catch ::::: " + msg);
			IncomingBufferMsg in = new IncomingBufferMsg();
			in.copyFrom(msg);

			in.setMethodHandler("oktell/uni/listener");
			in.setChannel(appConfig.getChannelBusBridge());
			in.setLogClass(BusWriteMsg.class);
			in.setOperationType(OperationType.WRITE);
			in.setEndPointId(EndpointType.OKTELL);
			in.setIsDirectInsert(false);
			appConfig.getQueueDirtyMsg().put(in);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "btx/uni/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonBtxListener(@RequestBody UniMinMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("btx listener catch ::::: " + msg);
			IncomingBufferMsg in = new IncomingBufferMsg();
			in.copyFrom(msg);

			in.setMethodHandler("btx/uni/listener");
			in.setChannel(appConfig.getChannelBusBridge());
			in.setLogClass(BusWriteMsg.class);
			in.setOperationType(OperationType.WRITE);
			in.setEndPointId(EndpointType.BTX);
			in.setIsDirectInsert(false);
			appConfig.getQueueDirtyMsg().put(in);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "btx/uni/listener2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void jsonBtxListener2(@RequestBody UniMinMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("btx listener2 catch ::::: " + msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "oktell/sub/err", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonOktellSubErr(@RequestBody ErrSubMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("oktell sub err catch ::::: " + msg);
			msg.setMethodHandler("oktell/sub/err");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.OKTELL);
			msg.setIsDirectInsert(true);
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

	@RequestMapping(value = "btx/sub/err", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonBtxSubErr(@RequestBody ErrSubMsg msg) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			System.out.println("btx sub err catch ::::: " + msg);
			msg.setMethodHandler("btx/sub/err");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.BTX);
			msg.setIsDirectInsert(true);
			appConfig.getQueueDirtyMsg().put(msg);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

}
