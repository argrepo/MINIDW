package com.anvizent.schedulers.configuration;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.anvizent.minidw.service.utils.QuartzSchedulerServiceImpl;
import com.anvizent.minidw.service.utils.SchedulerService;
import com.anvizent.schedulers.controller.MasterSlaveConnector;
import com.anvizent.schedulers.security.JwtAuthenticationTokenFilter;
import com.anvizent.schedulers.security.JwtTokenUtil;
import com.anvizent.schedulers.security.SchedulerServicesAuthenticationInterceptor;
import com.anvizent.schedulers.service.UserService;
import com.anvizent.schedulers.service.UserServiceImpl;
import com.datamodel.anvizent.helper.Constants;
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
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.anvizent.schedulers", "org.zerhusen.security" })
public class WebConfig extends WebMvcConfigurerAdapter {

	Logger logger = Logger.getLogger(WebConfig.class);

	@Autowired
	private ClientDbCredentials clientDbCredentials;
	
	@Autowired
	@Qualifier("app_dataSource")
	private DataSource app_dataSource;


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
	public UserDao userDao() {
		logger.info("in userdao creation page");

		String appDbHost = clientDbCredentials.getHostname();
		String appDbPort = clientDbCredentials.getPortnumber();
		String appDbName = clientDbCredentials.getClientDbSchema();
		return new UserDaoImpl(app_dataSource, clientDbCredentials.getClientDbUsername(), 
				clientDbCredentials.getClientDbPassword(), appDbHost, appDbPort, appDbName);
	}

	@Bean
	public CommonDao commonDao() {
		logger.info("in userdao creation page");
		return new CommonDaoImpl(app_dataSource);
	}

	@Bean
	public MasterDao masterDao() {
		return new MasterDaoImpl(app_dataSource);
	}

	@Bean
	public PackageDao packageDao() {
		return new PackageDaoImpl(app_dataSource);
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
		return new QuartzDaoImpl(app_dataSource);
	}

	@Bean
	SchedulerService quartzSchedulerServiceImpl() {
		return new QuartzSchedulerServiceImpl() {

			@Override
			public List<QuartzSchedulerJobInfo> getFilterScheduledJobsWithFilter(SchedulerFilterJobDetails groupName)
					throws SchedulerException {
				// TODO Au to-generated method stub
				return null;
			}
		};
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");// setCachePeriod(432000);
	}

	@Bean
	public MessageSource messageSource() {
		WildcardReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();

		/*
		 * Moved the messages folder to resources instead of WEB-INF because
		 * PathMatchingResourcePatternResolver was trying to look for the
		 * messages folder in /WEB-INF/classes instead of /WEB-INF
		 */
		messageSource.setBasenames(new String[] { "classpath*:/messages/*.properties" });
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(-1);
		return messageSource;
	}

	@Bean
	public MultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		return mr;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
		registry.addInterceptor(schedulerServicesAuthenticationInterceptor()).addPathPatterns("/services/**");
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
	SchedulerServicesAuthenticationInterceptor schedulerServicesAuthenticationInterceptor() {
		return new SchedulerServicesAuthenticationInterceptor();
	}

	@Bean
	JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}

	@Bean
	MasterSlaveConnector masterSlaveConnector() {
		return new MasterSlaveConnector();
	}
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*");
            }
        };
    }

}