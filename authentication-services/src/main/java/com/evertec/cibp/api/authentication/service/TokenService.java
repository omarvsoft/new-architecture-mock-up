package com.evertec.cibp.api.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evertec.cibp.api.authentication.model.domain.SSOToken;
import com.evertec.cibp.api.authentication.repository.SSOTokenRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class TokenService {

	@Autowired
	private SSOTokenRepository ssoTokenRepository;
	
	//private static final Log logger = LogFactory.getLog(TokenService.class);
	
	
	@HystrixCommand(groupKey = "getSsoToken", commandKey = "getSsoToken", threadPoolKey = "getSsoToken",
			commandProperties = {
	        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
	        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
	        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
	        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000") }, 
			threadPoolProperties = {
	        @HystrixProperty(name = "coreSize", value = "2"),
	        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000") })
	public SSOToken getSsoToken(String sso){
		//logger.info("info log");
		return ssoTokenRepository.findOne(sso);
	}
	
}
