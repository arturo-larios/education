package com.nvisium.moneyx.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.nvisium.moneyx.service.UserService;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

			http.authorizeRequests()
					.antMatchers("/is-user-logged-in", 
								 "/login/**",
								 "/dashboard/**",
								 "/register/**",
								 "/forgot-password/**",
								 "/admin/**",
								 "/payment/**",
								 "/dist/**",
								 "/console/**")
						.permitAll()
					.anyRequest()
						.authenticated()
				.and()
					.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/dashboard")
					.permitAll()
				.and()
					.logout()
					.permitAll()
				.and()
					.requestCache()
					.requestCache(new NullRequestCache())
				.and()
					.sessionManagement()
						.sessionFixation()
						.none()
				.and()
					.csrf()
						.disable()
					.headers()
						.defaultsDisabled()
						.addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection","0"));
						
					//.addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy-Report-Only",
					//		"default-src 'none'; script-src 'nonce-eiuenfiwnfio'; object-src 'none'; style-src 'none'; img-src 'none'; "
					//				+ "media-src 'none'; frame-src 'none'; font-src 'none'; connect-src 'none'; "
					//				+ "report-uri /admin/submit-csp-report"));
	}
	
	 @Bean 
	 public PasswordEncoder passwordEncoder(){
		 PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		 return encoder; 
	 }
	 
}
