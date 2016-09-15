package com.nvisium.moneyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nvisium.moneyx.controller.MoneyXErrorController;

@ComponentScan(basePackages = "com.nvisium.moneyx")
@EnableJpaRepositories(basePackages = "com.nvisium.moneyx.repository")
@SpringBootApplication
public class MoneyxApplication implements EmbeddedServletContainerCustomizer {

	public static void main(String[] args) {
		SpringApplication.run(MoneyxApplication.class, args);
	}

	@Autowired
	private ErrorAttributes errorAttributes;

	@Bean
	public MoneyXErrorController moneyXErrorController () {
		return new MoneyXErrorController(errorAttributes);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		
		((TomcatEmbeddedServletContainerFactory) container).addContextCustomizers(new TomcatContextCustomizer()
	    {
	        @Override
	        public void customize(Context context)
	        {
	            context.setUseHttpOnly(false);
	        }
	    });
		
	}

}


