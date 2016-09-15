package com.nvisium.moneyx;

import static org.hamcrest.CoreMatchers.containsString;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SdeTest extends MoneyXTestTemplate {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private ConfigurableApplicationContext c;
	
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
		// setupAuth();
	}
	
 	@Test
 	public void testSdeSecureCommunications() throws Exception {
 		assertThat(c.getEnvironment().getProperty("server.ssl.enabled"), containsString("true"));
 	}
 	
 	@Test
 	public void testSdeCacheHeaders() throws Exception {
 		String url = base + "/login";
 		String header1 = "no-cache";
 		System.out.println("Access Control Test to "+url);

		Response res = Jsoup.connect(url)
				.method(Method.GET)
				.validateTLSCertificates(false)
				.followRedirects(false)
				.execute();
 		assertThat(res.headers().toString(), containsString(header1));
 	}

}
