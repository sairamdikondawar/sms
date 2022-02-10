package com.sms.demo.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseModel implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 8934037170149026917L;

	public ResponseModel() {

	}

	public ResponseModel(String message, String error) {

		this.message = message != null ? message : "";
		this.error = error != null ? error : "";

	}

	private String message = "";

	private String error = "";

}
