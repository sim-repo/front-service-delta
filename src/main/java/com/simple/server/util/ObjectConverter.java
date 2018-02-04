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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;



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
	
	
	public synchronized static String prettyJson(String original) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(original, Object.class);
		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		return indented;
	}
	
	public static Document removeAllAttributes(Document thisDoc) throws XPathExpressionException {
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression expr = xpath.compile("//*[@*]");
	    NodeList result =(NodeList) expr.evaluate(thisDoc, XPathConstants.NODESET);
	    for (int i = 0; i < result.getLength(); i++) {
	        NamedNodeMap map = result.item(i).getAttributes();
	        for (int j = 0; j < map.getLength(); j++) {
	            map.removeNamedItem(map.item(j--).getNodeName());
	        }
	    }
	    return thisDoc;
	}
	
	
	public static Document convertXmlStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
	
	public static String convertDocumentToXmlString(Document doc) {
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer;
	        try {
	            transformer = tf.newTransformer();
	            // below code to remove XML declaration
	            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            StringWriter writer = new StringWriter();
	            transformer.transform(new DOMSource(doc), new StreamResult(writer));
	            String output = writer.getBuffer().toString();
	            return output;
	        } catch (TransformerException e) {
	            e.printStackTrace();
	        }
	        
	        return null;
	  }
	
	public static String removeNameSpacesFromXmlString(String xml) {
        try{
            String xslStr = String.join("\n",
                "<xsl:transform xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">",
                "<xsl:output version=\"1.0\" encoding=\"UTF-8\" indent=\"no\"/>",
                "<xsl:strip-space elements=\"*\"/>",                          
                "  <xsl:template match=\"@*|node()\">",
                "   <xsl:element name=\"{local-name()}\">",
                "     <xsl:apply-templates select=\"@*|node()\"/>",
                "  </xsl:element>",
                "  </xsl:template>",  
                "  <xsl:template match=\"text()\">",
                "    <xsl:copy/>",
                "  </xsl:template>",                                  
                "</xsl:transform>");

            // Parse XML and Build Document
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            Document doc = db.parse (is);                      

            // Parse XSLT and Configure Transformer
            Source xslt = new StreamSource(new StringReader(xslStr));
            Transformer tf = TransformerFactory.newInstance().newTransformer(xslt);

            // Output Result to String
            DOMSource source = new DOMSource(doc);
            StringWriter outWriter = new StringWriter();
            StreamResult strresult = new StreamResult( outWriter );        
            tf.transform(source, strresult);
            StringBuffer sb = outWriter.getBuffer(); 
            String finalstring = sb.toString();

            return(finalstring);

        } catch (Exception e) {
            System.out.println("Could not parse message as xml: " + e.getMessage());
        }
            return "";    
    }
	
}
