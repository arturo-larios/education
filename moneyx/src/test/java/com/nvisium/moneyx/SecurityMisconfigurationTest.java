package com.nvisium.moneyx;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
public class SecurityMisconfigurationTest extends MoneyXTestTemplate {
	
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
		// username = "user";
		// password = "user123";
		// setupAuth(userService);
	}
	
 	@Test
 	public void testSecMisconfigConsole() throws Exception {
 		String url = base + "/console/";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.execute();
 		assertThat(res.statusCode(), equalTo(302));
 	}
 	
 	@Test
 	public void testSecMisconfigAdmin() throws Exception {
 		String url = base + "/admin/";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.ignoreHttpErrors(true)
				.execute();
 		assertThat(res.statusCode(), equalTo(302));
 	}
 	
 	@Test
 	public void testSecMisconfigHttpHeaders() throws Exception {
 		String url = base + "/login";
 		String header1 = "X-Frame-Options";
 		String header2 = "nosniff";
 		String header3 = "X-XSS-Protection=1";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.execute();
 		assertThat(res.headers().toString(), allOf(containsString(header1),containsString(header2),containsString(header3)));
 	}
 	
 	@Test
 	public void testSecMisconfigVerboseErrors() throws Exception {
 		String url = base + "/payment/list-received/3a";
 		setupAuth(userService);
 		String test1 = "javax.persistence.PersistenceException";
 		String test2 = "java.lang.NumberFormatException";
 		
		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.ignoreHttpErrors(true)
				.execute();
		
		assertThat(res.body().toString(),not(anyOf(containsString(test1),containsString(test2))));
 	}
}
