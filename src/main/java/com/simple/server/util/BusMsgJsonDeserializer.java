package com.simple.server.util;

import java.io.IOException;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simple.server.domain.contract.IContract;

@Service("busMsgJsonDeserializer")
@SuppressWarnings("unchecked")
public class BusMsgJsonDeserializer extends JsonDeserializer<IContract> {

	public static final String NAME = "clazz";

	@Override
	public IContract deserialize(JsonParser jp, DeserializationContext context)throws IOException, JsonProcessingException {		
		 				
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = mapper.readTree(jp);
		if (root.has(NAME)) {
	            JsonNode clazzNode = root.get(NAME);	            	            	        	            	        	            	  		            		
        		IContract msg = null;
				try {
					msg = (IContract)mapper.readValue(root.toString(), getClass(clazzNode.asText(),IContract.class));
				} catch (ClassNotFoundException e) {								
					e.printStackTrace();
				}		            		 		            		 		            				            		 		            		
	            return msg;		    	            			    	           
	    }
		throw context.mappingException("FRONT-service: failed to de-serialize message.");	    		
	}
		
	public static <T> Class<T> getClass(final String className, Class<T> ifaceClass)  throws ClassNotFoundException {			    			
				final Class<T> clazz = (Class<T>) Class.forName(className).asSubclass(ifaceClass);
			    return clazz; 
	}
		
}