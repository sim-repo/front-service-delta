package com.simple.server.domain.contract.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Book {
	public String title;
    public String message;


    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", message=" + message + "]";
	}
    
    
}
