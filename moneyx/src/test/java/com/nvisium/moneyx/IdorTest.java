package com.nvisium.moneyx;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nvisium.moneyx.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdorTest extends MoneyXTestTemplate {
	
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
 	public void testMakePaymentIdor() throws Exception {
 		
 		// Custom variables
 		String url = base + "/payment/make-payment";
 		String testString = "New Phone";
 		
 		System.out.println("IDOR Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		
 		// Create & Send Request
 		Map<String,String> params = new HashMap<String,String>();
		params.put("event", "6");
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
 		assertThat(res.body(), not(containsString(testString)));
 	}
 	
 	@Test
 	public void testGetProfileIdor() throws Exception {
 		
 		// Custom variables
 		String url = base + "/profile/2";
 		String testString = "Cyrus";
 		
 		System.out.println("IDOR Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		
 		// Create & Send Request
		
		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(testString)));
 	}
 	
 	@Test
 	public void testPostProfileIdor() throws Exception {
 		
 		// Custom variables
 		String url = base + "/profile/2";
 		String testString = "TestFirst";
 		
 		System.out.println("IDOR Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		
 		// Create & Send Request
		Map<String,String> data = new HashMap<String,String>();
		data.put("username","cyrus");
		data.put("email","testemail@email.com");
		data.put("firstname", "TestFirst");
		data.put("lastname", "TestLast");
		if (!csrf.equals("")) {
			data.put("_csrf",csrf);
		}
		
		Response res = Jsoup.connect(url)
				.method(Method.POST)
				.validateTLSCertificates(false)
				.data(data)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(testString)));
 	}

}
