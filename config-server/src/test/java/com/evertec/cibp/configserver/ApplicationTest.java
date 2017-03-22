/**
 * 
 * java key store generated through
 * 
 *	keytool -genkeypair -alias aliasTest -keyalg RSA \
 *  -dname "CN=Configuration Server Unit Test,OU=MiBanco,O=EVERTEC,L=San Juan,S=Puerto Rico,C=PR" \
 *  -keypass JUnitConfigServSecret -keystore configServerTest.jks -storepass letmein  \
 *  -validity 14600
 * 
 * */

package com.evertec.cibp.configserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	final static Logger logger = LoggerFactory.getLogger(ApplicationTest.class);
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@BeforeClass
	public static void setUp() throws IOException{
		logger.info("Set UP. It is setting the necessary environment variables to execute the test.");
		
		Path testRepo 	  = Paths.get("./src/test/config-repo-test").toRealPath();
		Path keyStorePass = Paths.get("./src/test/keystore/configServerTest.jks").toRealPath();

		System.setProperty("spring.profiles.active", 		"junit");
		System.setProperty("security.basic.enabled", 		"false");
		System.setProperty("management.security.enabled", 	"false");
		System.setProperty("TEST_REPO_URI", 				testRepo.toUri().toString());
		System.setProperty("KEY_STORE_LOCATION", 			keyStorePass.toUri().toString());
		System.setProperty("KEY_STORE_ALIAS", 				"aliasTest");
		System.setProperty("KEY_STORE_PASSWORD", 			"letmein");
		System.setProperty("KEY_STORE_SECRET", 				"JUnitConfigServSecret");
	}
	
	@Test
	public void health() {
		logger.info("@Test. It is checking the health endpoint");
		LinkedHashMap<?, ?> response = this.restTemplate.getForObject("/health", LinkedHashMap.class);
		logger.info("/health response: "+response.toString());
		assertEquals("UP", response.get("status"));
	}
	
	
	@Test
	public void metrics(){
		logger.info("@Test. It is checking the metrics endpoint");
		LinkedHashMap<?, ?> response = this.restTemplate.getForObject("/metrics", LinkedHashMap.class);
		logger.info("/metrics response: "+response.toString());
		boolean isThereError = response.containsKey("error") || response.containsKey("exception");
		assertNotEquals(true, isThereError);
		assertTrue(response.containsKey("mem"));
	}
	
	@Test
	public void encrypt(){
		logger.info("@Test. It is checking the encrypt endpoint");
		String response = this.restTemplate.postForObject("/encrypt", "testvalue", String.class);
		logger.info("/encrypt response: "+response);
		assertNotNull(response);
		assertNotEquals("", response);
	}
	
	@Test
	public void defaultApp(){
		logger.info("@Test. It is checking the App application with default profile and master label");
		LinkedHashMap<?, ?> response = this.restTemplate.getForObject("/App/default/master", LinkedHashMap.class);
		logger.info(response.toString());
		boolean isThereError = response.containsKey("error") || response.containsKey("exception");
		assertNotEquals(true, isThereError);
		assertTrue(response.containsKey("name"));
		assertTrue(response.containsKey("profiles"));
		assertTrue(response.containsKey("label"));
		assertTrue(response.containsKey("version"));
		assertTrue(response.containsKey("propertySources"));
	}
	
	@Test
	public void testApp(){
		logger.info("@Test. It is checking the App test-app with junit profile and master label");
		LinkedHashMap<?, ?> response = this.restTemplate.getForObject("/test-app/junit/master", LinkedHashMap.class);
		logger.info("/test-app/junit/master response: "+response.toString());
		boolean isThereError = response.containsKey("error") || response.containsKey("exception");
		assertNotEquals(true, isThereError);
		assertTrue(response.containsKey("name"));
		assertTrue(response.containsKey("profiles"));
		assertTrue(response.containsKey("label"));
		assertTrue(response.containsKey("version"));
		assertTrue(response.containsKey("propertySources"));
		
		//validate if the decryption was successful
		List<?> propertiesSources = (List<?>) response.get("propertySources");
		assertTrue(!propertiesSources.isEmpty());
		propertiesSources.forEach(E->{
			if( E instanceof Map ){
				Map<?,?> sources = (Map<?,?>) E;
				Map<?,?> source  = (Map<?,?>) sources.get("source");
				source
					.forEach((K,V)->{
						assertTrue(!(K.toString().startsWith("invalid")));
						assertNotEquals("<n/a>", V);
					});
			}else{
				assert false : "It always must return a Map";
			}
		});
	}	
}

