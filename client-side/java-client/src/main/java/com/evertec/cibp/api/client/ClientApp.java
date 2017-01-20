package com.evertec.cibp.api.client;

import java.util.Properties;

import com.evertec.cibp.api.sdk.authorization.LoginFacade;
import com.evertec.cibp.api.sdk.common.model.domain.UserNameValidationResponse;

public class ClientApp {

	public static void main(String[] args) {
			
		Properties props = System.getProperties();
		
		props.setProperty("auth2.client.client-id", "mi-banco-web");
		props.setProperty("oauth2.client.client-secret", "web123");
		props.setProperty("oauth2.client.access-token-uri", "http://localhost:9090/authserver");
		props.setProperty("ci-bp.services.ahutorization-api.url", "http://localhost:8081/authentication/v1/");
		
		System.setProperties(props);

		
		LoginFacade login = new LoginFacade();
		
		Boolean result = 
				login.signClient(System.getProperty("auth2.client.client-id"),
								 System.getProperty("oauth2.client.client-secret"), 
						         System.getProperty("oauth2.client.access-token-uri"));
		
		if (!result) {
			System.out.println("Ivalid credentials");
			System.exit(0);
		}
		
		UserNameValidationResponse userNameValid = login.validateUser("omarvsoft");
		
		if (userNameValid.isSuccessful()) {
			System.out.println("uuuuuu!!!!");
			System.out.println(userNameValid);
			System.out.println(userNameValid.getCustomerProfile());
		} else {
			System.out.println(":=(");
			System.out.println(userNameValid.getErrorMessage());
		}
		
		
		
		
		
	}

}
