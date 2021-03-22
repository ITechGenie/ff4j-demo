package com.itechgenie.apps.toogles.config;

import org.ff4j.FF4j;
import org.ff4j.spring.boot.web.api.config.EnableFF4jSwagger;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableFF4jSwagger
@EnableSwagger2
@ConditionalOnClass({ FF4jDispatcherServlet.class })
@AutoConfigureAfter(FF4jConfig.class)
public class ItgConfig extends SpringBootServletInitializer {

	/**
	 * Definition of the servlet for web console
	 */
	@Bean
	@ConditionalOnMissingBean
	public FF4jDispatcherServlet getFF4jDispatcherServlet(FF4j ff4j) {
		FF4jDispatcherServlet ff4jConsoleServlet = new FF4jDispatcherServlet();
		ff4jConsoleServlet.setFf4j(ff4j);
		return ff4jConsoleServlet;
	}

	/**
	 * Mapping from FF4j to the endpoint you want
	 */
	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ServletRegistrationBean ff4jDispatcherServletRegistrationBean(FF4jDispatcherServlet ff4jDispatcherServlet) {
		return new ServletRegistrationBean(ff4jDispatcherServlet, "/ff4j-web-console/*");
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

}
