package com.anvizent.scheduler.configuration;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.anvizent.scheduler.security.JwtAuthenticationTokenFilter;
import com.anvizent.scheduler.security.JwtTokenUtil;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.MasterDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.impl.CommonDaoImpl;
import com.datamodel.anvizent.service.dao.impl.FileDaoImpl;
import com.datamodel.anvizent.service.dao.impl.MasterDaoImpl;
import com.datamodel.anvizent.service.dao.impl.PackageDaoImpl;
import com.datamodel.anvizent.service.dao.impl.QuartzDaoImpl;
import com.datamodel.anvizent.service.dao.impl.ScheduleDaoImpl;
import com.datamodel.anvizent.service.dao.impl.UserDaoImpl;
import com.datamodel.anvizent.service.model.ClientDbCredentials;

@Configuration
@EnableWebMvc
@ComponentScan({"com.anvizent.scheduler"})
public class WebConfig extends WebMvcConfigurerAdapter{
	
	Logger logger = Logger.getLogger(WebConfig.class);
	private @Value("${slaves.api.connection.timeout.in.seconds:5}") int slavesApiConnectionTimeoutInSeconds;
	@Autowired
	@Qualifier("app_dataSource")
	private DataSource appDataSource;
	
	@Autowired
	private ClientDbCredentials clientDbCredentials;
		
	
	@Bean
	public UserDao userDao() {
		String appDbHost = clientDbCredentials.getHostname();
		String appDbPort = clientDbCredentials.getPortnumber();
		String appDbName = clientDbCredentials.getClientDbSchema();
		return new UserDaoImpl(appDataSource, clientDbCredentials.getClientDbUsername(), 
				clientDbCredentials.getClientDbPassword(), appDbHost, appDbPort, appDbName);
	}
	
	@Bean
	public MasterDao masterDao() {
		return new MasterDaoImpl(appDataSource);
	}

	@Bean
	public CommonDao commonDao() {
		return new CommonDaoImpl(appDataSource);
	}
	@Bean
	public PackageDao packageDao() {
		return new PackageDaoImpl(appDataSource);
	}
	@Bean
	public FileDao fileDao() {
		return new FileDaoImpl();
	}
	@Bean
	public ScheduleDao scheduleDao() {
		return new ScheduleDaoImpl();
	}
	@Bean
	public QuartzDao quartzDao() {
		return new QuartzDaoImpl(appDataSource);
	}
	
	
	
	@Bean
    public MessageSource messageSource() {
		WildcardReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();
		
		/* 
		 * 
		 * Moved the messages folder to resources instead of WEB-INF because PathMatchingResourcePatternResolver
		 * was trying to look for the messages folder in /WEB-INF/classes instead of /WEB-INF
		 */
		messageSource.setBasenames(new String[]{"classpath*:/messages/*.properties"});
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(-1);
        return messageSource;
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
		registry.addInterceptor(jwtAuthenticationTokenFilter()).addPathPatterns("/user/**");
		/**
		 * intercept every url calling rest service to verify the client
		 * authentication
		 */
	}
	
	@Bean
	JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter();
	}
	
	@Bean
	JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}
	
	
	@Bean
	RestTemplate restTemplate() {
		logger.info("Slaves api Connection timeout " + slavesApiConnectionTimeoutInSeconds +" seconds");
		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		rf.setConnectTimeout(slavesApiConnectionTimeoutInSeconds * 1000);
		return restTemplate;
	}
	
}