package com.advertiseserver.domain;

import java.io.Serializable;

public class Ad implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Ad(String title, String description) {
		this.title = title;
		this.description = description;
	}
	public Ad() {
	}
	private String title;
	private String description;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
