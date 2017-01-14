package com.evertec.cibp.api.authentication;

import java.math.BigInteger;
import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.evertec.cibp.api.authentication.model.SSOToken;
import com.evertec.cibp.api.authentication.repository.SSOTokenRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class Application.
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
public class Application {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	/**
	 * Demo.
	 *
	 * @param sSOTokenRepository
	 *            the s SO token repository
	 * @return the command line runner
	 */
	@Bean
	public CommandLineRunner demo(SSOTokenRepository sSOTokenRepository) {
		return (args) -> {
			sSOTokenRepository
					.save(new SSOToken("A3FE4A124", "username=encarnacion&accountType=checking", new BigInteger("234"),
							"omarvsoft", new Date(System.currentTimeMillis()), "http://oao.bancopopular.com", true));
		};
	}

}
