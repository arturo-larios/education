package com.nvisium.moneyx;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessControlTest extends MoneyXTestTemplate {
	
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
		// username = "user";
		// password = "user123";
		// setupAuth(userService);
	}
 	
 	@Test
 	public void testAccessControlDashboard() throws Exception {
 		String url = base + "/dashboard";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.execute();
 		assertThat(res.statusCode(), equalTo(302));
 	}
 	
 	@Test
 	public void testAccessControlPayment() throws Exception {
 		String url = base + "/payment/list-received/3";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.execute();
 		assertThat(res.statusCode(), equalTo(302));
 	}
 	
}
