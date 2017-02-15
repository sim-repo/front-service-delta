package com.simple.server.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
	 private String _body;
	 
	public MultiReadHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		_body = "";
		BufferedReader bufferedReader = request.getReader();
		String line;
		while ((line = bufferedReader.readLine()) != null){
			_body += line;
		}
	 }

	@Override
	public ServletInputStream getInputStream() throws       IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_body.getBytes());
		return new ServletInputStream() {
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}
		};
	 }

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	 }
}