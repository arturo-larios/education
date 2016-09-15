package com.nvisium.moneyx;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.nvisium.moneyx.model.User;
import com.nvisium.moneyx.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MvcConfig.class,MoneyxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrokenAuthTest extends MoneyXTestTemplate {
	
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
		//setupAuth();
	}
	
 	@Test
 	public void testForPasswordEncoder() throws Exception {
 		userService.addRegularUser("testusername", "testpassword", "testemail@testemail.com", "color", "Broken", "Auth");
 		User testUser = userService.loadUser("testusername");
 		assertThat(testUser.getPassword(), not(containsString("testpassword")));
 	}
 	
 	@Test
 	public void testForHttpOnly() throws Exception {
 		
 			// Custom variables
 	 		String url = base + "/login";
 	 		String testString = "HttpOnly";
 	 		
 	 		// using htmlunit in place of jsoup to see raw set-cookie command
 	 		
 	 		WebClient wc = new WebClient();
 	 		wc.getOptions().setUseInsecureSSL(true);
 	 		HtmlPage page = wc.getPage(url);
 	 		List<NameValuePair> headers = page.getWebResponse().getResponseHeaders();
 	 		wc.close();
 			
 	 		// Check Response
 	 		assertThat(headers.toString(), containsString(testString));
 	}
 	
 	@Test
 	public void testForSessionTimeout() throws Exception {
 		assertThat(c.getEnvironment().getProperty("server.session.timeout"),equalTo("10"));
 	}

	@Test
	public void testForSessionFixation() throws Exception {
		Response res,res2;
		String url = base + "/login";
		String token1 = "";
		String token2 = "";
		try {
			System.out.println("Session Fixation Test for "+url);
			res = Jsoup.connect(base+"/login")
					.method(Method.GET)
					.validateTLSCertificates(false)
					.execute();
			
			token1=res.cookies().get("JSESSIONID");
			jsessionid=token1;
			if (res.parse().select("input[name=_csrf]").isEmpty()) {
	 			csrf = "";
	 		} else {
	 			csrf = res.parse().select("input[name=_csrf]").first().val();
	 		}
			
			
			Map<String,String> data = new HashMap<String,String>();
			data.put("username",username);
			data.put("password",password);
			if (!csrf.equals("")) {
				data.put("_csrf",csrf);
			}
			userService.updatePasswordByUsername(username, password);
			res2 = Jsoup.connect(base+"/login")
					.method(Method.POST)
					.validateTLSCertificates(false)
					.data(data)
					.cookie("JSESSIONID", jsessionid)
					.execute();
			
			if (res2.cookies().get("JSESSIONID") != null) {
				token2=res2.cookies().get("JSESSIONID");
			} else {
				token2=jsessionid;
			}
	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertThat(token1,not(equalTo(token2)));
	}
	
	@Test
	public void testLoginUserEnum() throws Exception {
		Response res;
		String url = base + "/login";
		String message1 = "";
		String message2 = "";
		try {
			System.out.println("User Enumeration Test for "+url);
			csrfCheck(url);
			Map<String,String> data = new HashMap<String,String>();
			data.put("username",username);
			data.put("password","tempPassword");
			if (!csrf.equals("")) {
				data.put("_csrf",csrf);
			}
			res = Jsoup.connect(base+"/login")
					.method(Method.POST)
					.validateTLSCertificates(false)
					.data(data)
					.cookie("JSESSIONID", jsessionid)
					.execute();
			
			message1 = res.body();
			
			csrfCheck(url);
			Map<String,String> data2 = new HashMap<String,String>();
			data2.put("username","userdoesnotexist");
			data2.put("password","tempPassword");
			if (!csrf.equals("")) {
				data2.put("_csrf",csrf);
			}
			res = Jsoup.connect(base+"/login")
					.method(Method.POST)
					.validateTLSCertificates(false)
					.data(data2)
					.cookie("JSESSIONID", jsessionid)
					.execute();
			
			message2 = res.body();
	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertThat(message1,equalTo(message2));
	}

}
