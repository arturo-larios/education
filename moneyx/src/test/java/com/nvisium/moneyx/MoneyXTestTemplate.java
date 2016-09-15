package com.nvisium.moneyx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.springframework.boot.context.embedded.LocalServerPort;

import com.nvisium.moneyx.service.UserService;

public class MoneyXTestTemplate {
	
	@LocalServerPort
	private int port;
	
	protected String base = "";
	protected String jsessionid = "";
	protected String csrf = "";
	protected String username = "user";
	protected String password = "user123";
	protected UserService userService;
	
 	protected void csrfCheck(String url) {
 		Response res;
 		try {
	 		if (jsessionid.equals("")) {
	 			res = Jsoup.connect(url)
	 					.method(Method.GET)
	 					.validateTLSCertificates(false)
	 					.execute();
	 		} else {
	 			res = Jsoup.connect(url)
	 					.method(Method.GET)
	 					.validateTLSCertificates(false)
	 					.cookie("JSESSIONID", jsessionid)
	 					.execute();
	 		}
	 		
	 		updateTokens(res);
	 		
 		} catch (IOException e) {
			e.printStackTrace();
		}
 		
 	}
	
	protected void setupAuth(UserService u) {
		userService = u;
		u.updatePasswordByUsername(username, password);
		csrfCheck(base+"/login");
		Response res;
		
		try {
			System.out.println("setupAuth for "+base+"/login");
			Map<String,String> data = new HashMap<String,String>();
			data.put("username",username);
			data.put("password",password);
			if (!csrf.equals("")) {
				data.put("_csrf",csrf);
			}
			
			if (jsessionid.equals("")) {
				res = Jsoup.connect(base+"/login")
						.method(Method.POST)
						.validateTLSCertificates(false)
						.data(data)
						.execute();
			} else {
				res = Jsoup.connect(base+"/login")
						.method(Method.POST)
						.validateTLSCertificates(false)
						.data(data)
						.cookie("JSESSIONID", jsessionid)
						.execute();
			}
	
			updateTokens(res);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateTokens(Response res) {
		try {
			if (res.cookies().get("JSESSIONID") != null) {
				jsessionid = res.cookies().get("JSESSIONID");
				System.out.println("Updated JSESSIONID: "+jsessionid);
			}
			
	 		Document doc = res.parse();
	 		if (doc.select("input[name=_csrf]").isEmpty()) {
	 			csrf = "";
	 		} else {
	 			csrf = doc.select("input[name=_csrf]").first().val();
	 			System.out.println("Updated CSRF Token: "+csrf);
	 		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
