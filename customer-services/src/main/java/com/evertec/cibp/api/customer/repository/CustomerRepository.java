package com.evertec.cibp.api.customer.repository;

import org.springframework.data.repository.CrudRepository;
import com.evertec.cibp.api.customer.model.Customer;


public interface CustomerRepository extends CrudRepository<Customer, String>{

}
