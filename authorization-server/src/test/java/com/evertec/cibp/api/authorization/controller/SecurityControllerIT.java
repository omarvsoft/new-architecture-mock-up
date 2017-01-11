package com.evertec.cibp.api.authorization.controller;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;

@RunWith(SpringJUnit4ClassRunner.class)
@RestClientTest(SecurityController.class)
public class SecurityControllerIT {
	
	@Autowired
    private SecurityController securityController;

    @Autowired
    private MockRestServiceServer server;
    
    @Test
    public void getVehicleDetailsWhenResultIsSuccessShouldReturnDetails()
            throws Exception {

    }

}
