package com.anvizent.packagerunner.configuration;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.anvizent.packagerunner.security.JwtAuthenticationTokenFilter;
import com.anvizent.packagerunner.security.JwtTokenUtil;
import com.anvizent.packagerunner.service.UserService;
import com.anvizent.packagerunner.service.UserServiceImpl;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.dao.impl.CommonDaoImpl;
import com.datamodel.anvizent.service.dao.impl.ETLAdminDaoImpl;
import com.datamodel.anvizent.service.dao.impl.FileDaoImpl;
import com.datamodel.anvizent.service.dao.impl.PackageDaoImpl;
import com.datamodel.anvizent.service.dao.impl.ScheduleDaoImpl;
import com.datamodel.anvizent.service.dao.impl.StandardPackageDaoImpl;
import com.datamodel.anvizent.service.dao.impl.UserDaoImpl;
import com.datamodel.anvizent.service.dao.impl.WebServiceDaoImpl;
import com.datamodel.anvizent.service.model.ClientDbCredentials;

@Configuration
@EnableWebMvc
@Import(AppProperties.class)
@ComponentScan({"com.anvizent.packagerunner", "com.datamodel.anvizent", "com.anvizent.minidw"})
public class WebConfig extends WebMvcConfigurerAdapter{
	
	Logger logger = Logger.getLogger(WebConfig.class);
	
	@Autowired
	private ClientDbCredentials clientDbCredentials;
	private @Value("${java.opt.params:}") String javaOptsParams;
	
	
	@Autowired
	@Qualifier("app_dataSource")
	private DataSource appDataSource;
	
	@Bean
	public UserService userService() {
		UserService userService = null;
		try {
			logger.info("UserService bean creation");
			userService = new UserServiceImpl();
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		}

		return userService;
	}
	

	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate; 
	}
 
	
	@Bean
	public UserDao userDao() {
		logger.info("in userdao creation page");
		String appDbHost = clientDbCredentials.getHostname();
		String appDbPort = clientDbCredentials.getPortnumber();
		String appDbName = clientDbCredentials.getClientDbSchema();
		Constants.Config.JAVA_OPT_PARAMS = javaOptsParams;
		return new UserDaoImpl(appDataSource, clientDbCredentials.getClientDbUsername(), 
				clientDbCredentials.getClientDbPassword(), appDbHost, appDbPort, appDbName);
	}

	@Bean
	public PackageDao packageDao() {
		logger.info("in userdao creation page");
		return new PackageDaoImpl(appDataSource);
	}
	
	@Bean
	public FileDao fileDao() {
		logger.info("in userdao creation page");
		return new FileDaoImpl();
	}

	@Bean
	public ScheduleDao scheduleDao() {
		logger.info("in ScheduleDao creation page");
		return new ScheduleDaoImpl();
	}
	
	@Bean
	public WebServiceDao webServiceDao() {
		logger.info("in WebServiceDao creation page");
		return new WebServiceDaoImpl(appDataSource);
	}

	@Bean
	public ETLAdminDao eTLAdminDao() {
		logger.info("in WebServiceDao creation page");
		return new ETLAdminDaoImpl(appDataSource);
	}
	@Bean
	public CommonDao commonDao() {
		logger.info("in commonDao creation page");
		return new CommonDaoImpl(appDataSource);
	}
	

	@Bean
	public JdbcTemplate getCommonJdbcTemplate() {
		return new JdbcTemplate(appDataSource);
	}
	
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
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
	public StandardPackageDao standardPackageDao() {
		logger.info("in ScheduleDao creation page");
		return new StandardPackageDaoImpl(appDataSource);
	}
	
}