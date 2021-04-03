package com.itechgenie.apps.toogles.config;

import javax.sql.DataSource;

import org.ff4j.FF4j;
import org.ff4j.parser.yaml.YamlParser;
import org.ff4j.springjdbc.store.EventRepositorySpringJdbc;
import org.ff4j.springjdbc.store.FeatureStoreSpringJdbc;
import org.ff4j.springjdbc.store.PropertyStoreSpringJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.itechgenie.apps.toogles.stores.CustomJdbcFeatureStore;
import com.itechgenie.apps.toogles.stores.CustomJdbcQueryBuilder;

@Configuration
public class FF4jConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FF4jConfig.class);

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.username}")
	private String jdbcUserName;

	@Value("${spring.datasource.password}")
	private String jdbcPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String jdbcDriver;

	 
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource jdbc = new DriverManagerDataSource();
		jdbc.setDriverClassName(jdbcDriver);
		jdbc.setUrl(jdbcUrl);
		jdbc.setPassword(jdbcPassword);
		jdbc.setUsername(jdbcUserName);
		return jdbc;
	}

	@Bean
	public FF4j getFF4j(DataSource dataSource) {
		LOGGER.info("Configuring FF4J !!");
		FF4j ff4j = new FF4j(new YamlParser(), "ff4j-init-dataset.yml").audit(true);
		
		CustomJdbcQueryBuilder queryBuilder = new CustomJdbcQueryBuilder("", "");
		
		CustomJdbcFeatureStore fjdbc = new CustomJdbcFeatureStore(dataSource) ;
		fjdbc.setQueryBuilder(queryBuilder);
		PropertyStoreSpringJdbc pjdbc = new PropertyStoreSpringJdbc(dataSource) ;
		pjdbc.setQueryBuilder(queryBuilder);
		EventRepositorySpringJdbc ejdbc = new EventRepositorySpringJdbc(dataSource) ;
		ejdbc.setQueryBuilder(queryBuilder);
		
		ff4j.setFeatureStore(fjdbc);
		ff4j.setPropertiesStore(pjdbc);
		ff4j.setEventRepository(ejdbc);
		
		ff4j.audit(true);
		ff4j.setEnableAudit(true);
		ff4j.setAutocreate(true);

	//	RedisConnection connection = new RedisConnection(getConnection()) ;
	//	FF4JCacheManager cacheManager = new FF4jCacheManagerRedis(connection);

		return ff4j;
	}

}
