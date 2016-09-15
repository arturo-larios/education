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
public class InjectionTest extends MoneyXTestTemplate {
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
 	public void testListReceivedSqlInjection() throws Exception {
 		
 		// Custom variables
 		String url = base + "/payment/list-received/3";
 		String payload = " OR 1=1";
 		String testString = "Equipment for the band";
 		
 		System.out.println("SQL Injection Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
		
		Response res = Jsoup.connect(url+payload)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.ignoreHttpErrors(true)
				.followRedirects(false)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(testString)));
 	}
 	
 	@Test
 	public void testListSentSqlInjection() throws Exception {
 		
 		// Custom variables
 		String url = base + "/payment/list-sent/3";
 		String payload = " OR 1=1";
 		String testString = "Equipment for the band";
 		
 		System.out.println("SQL Injection Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
		
		Response res = Jsoup.connect(url+payload)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.ignoreHttpErrors(true)
				.followRedirects(false)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(testString)));
 	}
 	
 	@Test
 	public void testPaymentBalanceSqlInjection() throws Exception {
 		
 		// Custom variables
 		String url = base + "/payment/balance";
 		String payload = " where id=1--";
 		String testString = "Balance updated successfully!";
 		
 		System.out.println("SQL Injection Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		// Create & Send Request
 		Map<String,String> params = new HashMap<String,String>();
		params.put("creditcard", "4111+1111+1111+1111");
		params.put("fullname", "Jared+Test");
		params.put("expirationdate", "02+%2F+20");
		params.put("cvccode", "123");
		params.put("amount", "300"+payload);
		if (!csrf.equals("")) {
			params.put("_csrf", csrf);
		}
		Response res = Jsoup.connect(url)
				.method(Method.POST)
				.validateTLSCertificates(false)
				.ignoreHttpErrors(true)
				.data(params)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.body(), not(containsString(testString)));
 	}
}
