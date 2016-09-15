package com.nvisium.moneyx;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nvisium.moneyx.service.UserService;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XssTest extends MoneyXTestTemplate {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private ConfigurableApplicationContext c;
	
	@Autowired
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		base = "http://localhost:"+port;
		if (c.getEnvironment().getProperty("server.ssl.enabled")!= null) {
			if (c.getEnvironment().getProperty("server.ssl.enabled").equals("true"))
				base = "https://localhost:"+port;
		}
		
		// Enable if tests require authentication (defaults are user:user123)
		// this.username = "user";
		// this.password = "user123";
		setupAuth(userService);
		
	}
	
 	@Test
 	public void testEventSearchXSS() throws Exception {
 		
 		// Custom variables
 		String url = base + "/event/search";
 		String payload = "test\"><script>alert(1)</script>";
 		
 		System.out.println("XSS Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		
 		// Create & Send Request
 		Map<String,String> params = new HashMap<String,String>();
		params.put("q", payload);
		if (!csrf.equals("")) {
			params.put("_csrf", csrf);
		}
		
		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.data(params)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(payload)));
 	}
 	
 	@Test
 	public void testEditProfileXSS() throws Exception {
 		
 		// Custom variables
 		String url = base + "/profile/3";
 		String payload = "\"><script>alert(2)</script>";
 		
 		System.out.println("XSS Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		
 		// Create & Send Request
 		Map<String,String> params = new HashMap<String,String>();
		params.put("username", username);
		params.put("firstname", "Student"+payload);
		params.put("lastname", "User"+payload);
		params.put("email", "user%40test.com"+payload);
		if (!csrf.equals("")) {
			params.put("_csrf", csrf);
		}
		
		Response res = Jsoup.connect(url)
				.method(Method.POST)
				.validateTLSCertificates(false)
				.data(params)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(payload)));
 	}
	
}