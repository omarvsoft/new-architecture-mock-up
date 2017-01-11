package com.evertec.cibp.api.authentication;

import java.math.BigInteger;
import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.evertec.cibp.api.authentication.model.SSOToken;
import com.evertec.cibp.api.authentication.repository.SSOTokenRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class Application.
 */
@SpringBootApplication
@EnableSwagger2
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
							"", new Date(System.currentTimeMillis()), "http://oao.bancopopular.com", true));
		};
	}

}
