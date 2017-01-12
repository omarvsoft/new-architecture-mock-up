package com.evertec.cibp.api.customer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.evertec.cibp.api.customer.model.Customer;
import com.evertec.cibp.api.customer.repository.CustomerRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
	
	@Bean
	public CommandLineRunner demo(CustomerRepository customerRepository) {
		return (args) -> {
			customerRepository
					.save(new Customer("0001", 1, "omarvsoft", "ASD34DA1231AWRYE5Q6S65ASESH87", 
							"AS3412", "omarvsoft@gmail.com","1985-08-16", "1", "banco", "asw2"));
		};
	}

}
