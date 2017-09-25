package com.simple.server.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.simple.server.domain.contract.AContract;

public class ObjectConverter {
	private ObjectConverter(){}
		
	public static String objectToJson(Object object){
		StringWriter writer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(writer, object);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return writer.toString();
	}
	
	public static Object jsonToObject(String json, Object object){
		ObjectMapper mapper = new ObjectMapper();
		final ObjectReader reader = mapper.reader();
		try {
			object = reader.forType(object.getClass()).readValue(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	
	public synchronized static <T extends AContract> T requestToObject(HttpServletRequest request, Class<T> clazz){
		ObjectMapper mapper = new ObjectMapper();
		T t = null; 
		try {
			t = mapper.readValue(request.getInputStream(), clazz);
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     return t;
	}
	
	
	
	public synchronized static String xmlToJson(String xml) throws Exception{
		JSONObject jObject = XML.toJSONObject(xml);
	    ObjectMapper mapper = new ObjectMapper();	   
	    Object json = mapper.readValue(jObject.toString(), Object.class);
	    String res = mapper.writeValueAsString(json);
		return res;
	}
	
	
	
	public synchronized static String jsonToXml(String json, Boolean useDeclaration) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String,Object>>(){});
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, useDeclaration);	

		String xml = xmlMapper.writeValueAsString(map);		
		return xml;
	}
	
	
	public synchronized static Object xmlToObject(String xml, Class clazz) throws Exception{
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);
		Object obj = unmarshaller.unmarshal(reader);
		return obj;
	}
	
}
