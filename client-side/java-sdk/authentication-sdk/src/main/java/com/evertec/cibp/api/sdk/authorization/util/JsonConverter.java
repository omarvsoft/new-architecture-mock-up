package com.evertec.cibp.api.sdk.authorization.util;

import com.evertec.cibp.api.sdk.common.model.security.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonConverter {
	
	public static <T> String objectToJson(T object) {	
		return null;
	}
	
	
	public static <T> T jsonToObject(String json, Class<T> tClass) {
		
		Gson gson = new Gson();
		
		T object = null;
		
		try {
						
			object =  gson.fromJson(json, tClass);
			
			System.out.println(object);
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} 
		
		return (T) object;
	}
	
	public static Token getJsonAsToken(String json) {
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson( json, JsonObject.class);
		
		JsonElement refresh = jsonObject.get("refresh_token");
		
		Long expiresIn   = jsonObject.get("expires_in").getAsLong();
		String tokenType = jsonObject.get("token_type").getAsString();
		String refreshToken = null != refresh ? refresh.getAsString() : "";
		String accessToken = jsonObject.get("access_token").getAsString();
		
		Token token = new Token(expiresIn, tokenType, refreshToken, accessToken);
		
		return token;
	}
	
	
	
}
