package com.anvizent.scheduler.configuration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.datamodel.anvizent.service.model.ClientDbCredentials;

/**
 * 
 * define Data Source and Transaction Management
 * 
 * @author rajesh.anthari
 *
 */
@Configuration
@Import(AppProperties.class)
@EnableTransactionManagement
public class DataSourceConfig {
	protected final Log log = LogFactory.getLog(DataSourceConfig.class);

	public DataSourceConfig() {
		log.info("DataSource Config loaded.");
	}

	@Bean
	public PlatformTransactionManager user_transactionManager() throws NamingException {
		return new DataSourceTransactionManager(app_dataSource());
	}

	@Bean(destroyMethod="")
	public DataSource app_dataSource() throws NamingException {
		DataSource dataSource = null;
		InitialContext in = new InitialContext();
		dataSource = (DataSource) in.lookup("java:comp/env/jdbc/anvizent");
		return dataSource;
	}
	@Bean
	public ClientDbCredentials clientDbCredentials() throws NamingException {
		ClientDbCredentials clientDbCredentials = new ClientDbCredentials();
		BasicDataSource dataSource = (BasicDataSource)app_dataSource();
		
		String[] appDbInfo = StringUtils.substringBetween(dataSource.getUrl(), "//", "/").split(":");
		clientDbCredentials.setHostname(appDbInfo[0]);
		clientDbCredentials.setPortnumber(appDbInfo[1]);
		clientDbCredentials.setClientDbUsername(dataSource.getUsername());
		clientDbCredentials.setClientDbPassword(dataSource.getPassword());
		String appDbName = StringUtils.substringAfterLast(dataSource.getUrl(), "/");
		appDbName = appDbName.contains("?") ? StringUtils.substringBefore(appDbName, "?"):appDbName;
		clientDbCredentials.setClientDbSchema(appDbName);
		log.info(clientDbCredentials);
		/*
		log.info("max active : " + dataSource.getMaxTotal());
		log.info("max idle : " + dataSource.getMaxIdle());
		log.info("initial size : " + dataSource.getInitialSize());
		log.info("min idle : " + dataSource.getMinIdle());*/
		return clientDbCredentials;
	}
}
