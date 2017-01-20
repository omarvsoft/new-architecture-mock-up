package com.evertec.cibp.api.authentication.repository;

import org.springframework.data.repository.CrudRepository;

import com.evertec.cibp.api.authentication.model.domain.SSOToken;

/**
 * The Interface SSOTokenRepository.
 */
public interface SSOTokenRepository extends CrudRepository<SSOToken, String> {

}
