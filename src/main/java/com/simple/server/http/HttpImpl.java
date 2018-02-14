package com.simple.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.util.Base64;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.RedirectRouting;
import com.simple.server.util.ObjectConverter;

import org.springframework.http.MediaType;
public class HttpImpl {

	public static ResponseEntity<String> get(RedirectRouting redirect, String params) throws Exception {
		
		ResponseEntity<String> res = null;
		Boolean useAuth = false;
		if(params==null)
			params="";
		try {			
			
			if (!(redirect.getUseAuth()==null)){
				useAuth = redirect.getUseAuth();
			}
			
			if (useAuth){
				res = doGetNTLM(redirect.getUrl()+params, "aplication/json");				
			}
			else {		
				URI uri = new URI(redirect.getUrl()+params);
				RestTemplate restTemplate = new RestTemplate(getSimplestClientHttpRequestFactory());				 
				HttpEntity<String> entity = new HttpEntity<String>("", createHeaders());
				res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			}												
			
		} catch (HttpStatusCodeException e) {					
			String json = ObjectConverter.xmlToJson(e.getResponseBodyAsString());			
			return new ResponseEntity<String>(json, createHeaders(), e.getStatusCode());
		}
		return res;
	}
	
	

	@SuppressWarnings("all")
	public static ResponseEntity<String> doGetNTLM(String url, String contentType) throws Exception{
		
				
		DefaultHttpClient httpclient = new DefaultHttpClient();
				
		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {                
                    request.addHeader("Accept-Encoding", "gzip, deflate");
                    request.addHeader("Accept", "*/*");
                    request.addHeader("Accept-Language", " ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");             
                    request.addHeader("Content-Type", contentType);                                
            }
        });
			
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url); 
        
        final RequestConfig params = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        httpGet.setConfig(params);
		        
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new NTCredentials(AppConfig.ACC, AppConfig.PSW, AppConfig.WORKSTATION, AppConfig.DOMEN));

        List<String> authtypes = new ArrayList<String>();
        authtypes.add(AuthPolicy.NTLM);      
        httpclient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF,authtypes);

        localContext.setAttribute(ClientContext.CREDS_PROVIDER, credsProvider);
        HttpResponse response = httpclient.execute(httpGet, localContext);        
        
        StringBuilder body = null;
        String sBody = "";
        if( response.getEntity() != null ) {
        	body = new StringBuilder();
        	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));      
            String line = "";
            while ((line = rd.readLine()) != null) {                 
            	body.append(line);
            }
        	response.getEntity().consumeContent();     
        	sBody = body.toString();
        }
                
        httpclient.getConnectionManager().shutdown();
        
        final Integer httpCode = response.getStatusLine().getStatusCode();     
        
        return new ResponseEntity<String>(sBody, createHeaders(), HttpStatus.valueOf(httpCode));               
	}
	
	
	
	public static HttpHeaders createHeaders(){
		return new HttpHeaders(){
			{setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));}
		};
	}
	
	public static HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
				setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));				
			}
		};
	}
	
	
	
	private static ClientHttpRequestFactory getSimpleClientHttpRequestFactory() {
	    int timeout = 5000;
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory= new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(timeout);
	    return clientHttpRequestFactory;
	}	
	
	
	private static ClientHttpRequestFactory getClientHttpRequestFactory() {
	    int timeout = 5000;
	    RequestConfig config = RequestConfig.custom()
	      .setConnectTimeout(timeout)
	      .setConnectionRequestTimeout(timeout)
	      .setSocketTimeout(timeout)
	      .build();
	    CloseableHttpClient client = HttpClientBuilder
	      .create()
	      .setDefaultRequestConfig(config)
	      .build();
	    return new HttpComponentsClientHttpRequestFactory(client);
	}
	
	private static ClientHttpRequestFactory getSimplestClientHttpRequestFactory(){
		CloseableHttpClient httpClient = HttpClientBuilder
            .create()
            .build();
    	return new HttpComponentsClientHttpRequestFactory(httpClient);
	}
}
