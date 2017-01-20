package com.evertec.cibp.api.sdk.authorization.client;

import com.evertec.cibp.api.sdk.common.model.domain.SSOToken;
import com.evertec.cibp.api.sdk.common.model.domain.UserNameValidationResponse;

public interface LoginClient {
	UserNameValidationResponse validateUserName(String userName);
}
