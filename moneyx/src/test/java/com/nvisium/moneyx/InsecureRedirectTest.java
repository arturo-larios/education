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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsecureRedirectTest extends MoneyXTestTemplate {
	@LocalServerPort
	private int port;
	
	@Autowired
	private ConfigurableApplicationContext c;
	
	//@Autowired
	//private UserService userService;
	
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
		//setupAuth(userService);
	}
	
	@Test
	public void testInsecureRedirectRegister() throws Exception {
 		// Custom variables
 		String url = base + "/register";
 		String testString = "Location=https://www.nvisium.com";
 		
 		System.out.println("Insecure Redirection Test to "+url);
 		
 		// Check for CSRF
 		csrfCheck(url);
 		// Create & Send Request
 		Map<String,String> params = new HashMap<String,String>();
		params.put("username", "newuser");
		params.put("password", "testpass");
		params.put("email", "testuser@test.com");
		params.put("answer", "orange");
		params.put("firstname", "TestFirst");
		params.put("lastname", "TestLast");
		params.put("next", "https://www.nvisium.com");
		if (!csrf.equals("")) {
			params.put("_csrf", csrf);
		}
		Response res = Jsoup.connect(url)
				.method(Method.POST)
				.validateTLSCertificates(false)
				.ignoreHttpErrors(true)
				.followRedirects(false)
				.data(params)
				.cookie("JSESSIONID", jsessionid)
				.execute();

 		// Check Response
 		assertThat(res.headers().toString(), not(containsString(testString)));
		
	}
}
