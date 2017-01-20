package com.evertec.cibp.api.sdk.common.model;

public class JsonResponse {
	
	private String json;
	private Integer httpCode;
	private String httpMessage;
	private String exceptionMessage;
	private Boolean valid;
	
	public JsonResponse(String json) {
		this.json = json;
		this.valid = true;
	}
	
	public JsonResponse(Integer httpCode, String httpMessage, String exceptionMessage) {
		this.httpCode = httpCode;
		this.httpMessage = httpMessage;
		this.exceptionMessage = exceptionMessage;
		this.valid = false;
	}
	
	public String getJson() {
		return json;
	}
	public Integer getHttpCode() {
		return httpCode;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public Boolean isValid() {
		if (valid == null) {
			valid = false;
		}
		return valid;
	}
	public String getHttpMessage() {
		return httpMessage;
	}
	
	public String getErrorMessage(){
		return 
		"Http code: "
		.concat(null != httpCode ? httpCode.toString() : "NA")
		.concat(" HttpMessage : ")
		.concat(null != httpMessage ? httpMessage : "")
		.concat(" exceptionMessage: ")
		.concat(null != exceptionMessage ? exceptionMessage : "");
	}
	
}
