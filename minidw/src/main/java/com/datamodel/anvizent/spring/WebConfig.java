package com.datamodel.anvizent.spring;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.context.support.ServletContextAttributeExporter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.anvizent.client.scheduler.listner.ClientScheduler;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.filter.CookieHandler;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.interceptor.MinidwAuthenticationInterceptor;
import com.datamodel.anvizent.interceptor.NavigationInterceptor;
import com.datamodel.anvizent.interceptor.WebServiceInterceptor;
import com.datamodel.anvizent.security.UserNotAuthenticatedException;
import com.datamodel.anvizent.spring.TypeConverter.StringToIndustryConverter;
import com.datamodel.anvizent.validator.CustomPackageFormValidator;
import com.datamodel.anvizent.validator.StandardPackageFormValidator;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Spring MVC configuration.
 * 
 * define web related configurations
 * 
 * @author rakesh.gajula
 *
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@Import(AppProperties.class)
@ComponentScan({"com.anvizent.minidw"})
public class WebConfig extends WebMvcConfigurerAdapter {
	protected final Log log = LogFactory.getLog(WebConfig.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private @Value("${service.contextPath:}") String contextPath;
	private @Value("${deployment.type:cloud}") String deploymentType;
	private @Value("${storage.location:}") String storageLocation;
	private @Value("${schduler.default.start:false}") String schedulerDefaultStart;
	
	private @Value("${minidw.version:NotSpecified}") String version;

	public WebConfig() {
		log.debug("Web Configuration loaded.");
	}

	/**
	 * Turn off passing parameters automatically on redirects.
	 * 
	 * @param adapter
	 */
	@Autowired
	public void setArgumentResolvers(RequestMappingHandlerAdapter adapter) {
		adapter.setIgnoreDefaultModelOnRedirect(true);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToIndustryConverter());
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(Constants.Config.RESOURCES + "**").addResourceLocations(Constants.Config.RESOURCES).setCachePeriod(432000); // 5 days
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
		registry.addInterceptor(navigationInterceptor()).addPathPatterns("/adt/**", "/adv/**", "/admin/**", "/userProfile", "/accessDenied");
		registry.addInterceptor(webServiceInterceptor()).addPathPatterns("/app/**", "/app_Admin/**","/sadmin/**");
		registry.addInterceptor(new CookieHandler());
		/**
		 * intercept every url calling rest service to verify the client
		 * authentication
		 */
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SitePreferenceHandlerMethodArgumentResolver());
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
	}

	/*
	 * @Bean(name = "multipartResolver") public StandardServletMultipartResolver
	 * resolver() { return new StandardServletMultipartResolver(); }
	 */
	@Bean
	public NavigationInterceptor navigationInterceptor() {
		return new NavigationInterceptor();
	}

	@Bean
	public WebServiceInterceptor webServiceInterceptor() {
		return new WebServiceInterceptor();
	}
	
	@Bean
	public MinidwAuthenticationInterceptor anvizentAuthenticationInterceptor() {
		return new MinidwAuthenticationInterceptor();
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(new Locale("en","US"));
		return resolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		return viewResolver;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions(new String[] { "/WEB-INF/tiles-defs.xml" });
		return configurer;
	}

	@Bean
	public MessageSource messageSource() {
		WildcardReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();

		/*
		 * Moved the messages folder to resources instead of WEB-INF because
		 * PathMatchingResourcePatternResolver was trying to look for the
		 * messages folder in /WEB-INF/classes instead of /WEB-INF
		 */
		messageSource.setBasenames(new String[] { "classpath*:/messages/applicationMessages.properties" });
		messageSource.setUseCodeAsDefaultMessage(true);
		//messageSource.setCacheSeconds(-1);
		messageSource.setDefaultEncoding(DEFAULT_ENCODING);
		return messageSource;
	}

	@Bean
	public RequestDataValueProcessor requestDataValueProcessor() {
		return new CsrfRequestDataValueProcessor();
	}

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		PortalExceptionResolver resolver = new PortalExceptionResolver();

		Properties exceptions = new Properties();
		exceptions.put(UserNotAuthenticatedException.class.getName(), "/errors/notAuthenticated");

		resolver.setExceptionMappings(exceptions);
		resolver.setDefaultErrorView("/errors/500");
		return resolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		return mr;
	}

	@Bean(name = "filterMultipartResolver")
	public CommonsMultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver filterMultipartResolver = new CommonsMultipartResolver();
		filterMultipartResolver.setDefaultEncoding("utf-8");
		return filterMultipartResolver;
	}

	@Bean
	public ServletContextAttributeExporter servletContextAttributeExporter() {
		ServletContextAttributeExporter servletContext = new ServletContextAttributeExporter();
		SimpleDateFormat buildFmt = new SimpleDateFormat("yyyyMMddHHmmss");
		// prevent caching of CSS/JS in test/dev
		String build = buildFmt.format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("build", build);
		String deploymentMessages = "";
		Boolean isWebApp = true; 
		
		if (StringUtils.isBlank(contextPath)) {
			deploymentMessages += Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_CONTEXT_PATH; 
		} 
		if (!(Constants.Config.DEPLOYMENT_TYPE_CLOUD.equals(deploymentType) || Constants.Config.DEPLOYMENT_TYPE_HYBRID.equals(deploymentType)
				|| Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType))) {
			deploymentMessages+= Constants.Config.BREAK_LINE + Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INAVLID_TYPE;
		}
		if (Constants.Config.DEPLOYMENT_TYPE_HYBRID.equals(deploymentType)) { 
			isWebApp = false;
		} 
		if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType)) {
			if (StringUtils.isBlank(storageLocation)) {
				deploymentMessages+= Constants.Config.BREAK_LINE +Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_STORAGE_LOCATION;
			} else {
				File file = new File(storageLocation);
				if ( !file.exists() ) {
					deploymentMessages+= Constants.Config.BREAK_LINE +Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_FOLDER_STORAGE_EXIST;
				} else if (!file.isDirectory()) { 
					deploymentMessages+= Constants.Config.BREAK_LINE +Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_FOLDER_STORAGE_LOCATION;
				}else{
					Constants.Config.STORAGE_LOCATION =  storageLocation;
				}
			} 
		
		}
		if (version.equals("NotSpecified")) {
			deploymentMessages+= Constants.Config.BREAK_LINE + Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_VERSION_DETAILS;
		}
		map.put(Constants.Config.DEPLOYMENT_TYPE_MESSAGE, deploymentMessages);
		
		map.put("isWebApp", isWebApp);
		map.put("deploymentType", deploymentType);
		map.put("version", version.equals("NotSpecified") ?"Vesrion details not found":version);
		servletContext.setAttributes(map);
		return servletContext;
	}
	
	@Bean
	public RestTemplateUtilities anvizentServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/package");
	}

	@Bean
	public RestTemplateUtilities anvizentServiceslbRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath, @Value("${service.contextPath.lb:}") String serviceContextPathlb) {
		String serviceUrl = StringUtils.isNotBlank(serviceContextPathlb) ? serviceContextPathlb : serviceContextPath;
		log.info("anvizentServiceslbRestTemplateUtilities  -- > serviceUrl --> " + serviceUrl);
		return new RestTemplateUtilities(serviceUrl , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/package");
	} 

	@Bean
	public RestTemplateUtilities etlAdminServiceslbRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath, @Value("${service.contextPath.lb:}") String serviceContextPathlb) {
		String serviceUrl = StringUtils.isNotBlank(serviceContextPathlb) ? serviceContextPathlb : serviceContextPath;
		log.info("etlAdminServicesRestTemplateUtilities  -- > serviceUrl --> " + serviceUrl);
		return new RestTemplateUtilities(serviceUrl , Constants.AnvizentWSURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin");
	}
	@Bean
	public RestTemplateUtilities etlAdminServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin");
	}

	@Bean
	public RestTemplateUtilities userServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/userProfile");
	}
	@Bean
	public RestTemplateUtilities clientInstantScriptExecutionDataRestController(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin/instantScriptExcecution");
	}

	@Bean
	public RestTemplateUtilities loginServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , "/loginService");
	}

	@Bean
	public RestTemplateUtilities scheduleServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/package/schedule");
	}

	@Bean
	public RestTemplateUtilities scheduleServiceslbRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath, @Value("${service.contextPath.lb:}") String serviceContextPathlb) {
		String serviceUrl = StringUtils.isNotBlank(serviceContextPathlb) ? serviceContextPathlb : serviceContextPath;
		log.info("scheduleServiceslbRestTemplateUtilities  -- > serviceUrl --> " + serviceUrl);
		return new RestTemplateUtilities(serviceUrl , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/package/schedule");
	} 


	@Bean
	public RestTemplateUtilities crossReferenceRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/crossreference");
	}
	

	@Bean
	public RestTemplateUtilities hierarchicalRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL  + "/hierarchical");
	}

	
	@Bean
	public RestTemplateUtilities appDBVersionTableScriptRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/appDbVersionTableScripts");
	}
	
	@Bean
	public RestTemplateUtilities eltRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ADMIN_SERVICES_BASE_URL  + "/eltconfig");
	}

	@Bean
	public StandardPackageFormValidator standardPackageFormValidator() {
		return new StandardPackageFormValidator();
	}

	@Bean
	public CustomPackageFormValidator customPackageFormValidator() {
		return new CustomPackageFormValidator();
	}

	@Bean
	public RestTemplateUtilities webServiceApiOAuthRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/webServiceApiOAuth");
	}

	@Bean
	public RestTemplateUtilities commonServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.COMMON_SERVICES_BASE_URL);
	}
	
	
	@Bean
	public RestTemplateUtilities apisDataRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL + "/apisData");
	}

	@Autowired
	@Bean
	ClientScheduler clientScheduler(StandardPackageProcessor standardPackageProcessor,@Value("${service.contextPath:}") String serviceContextPath) {
		ClientScheduler clientScheduler = new ClientScheduler();
		clientScheduler.setPackageRestUtilities(anvizentServicesRestTemplateUtilities(serviceContextPath));
		clientScheduler.setLoginRestUtilities(loginServicesRestTemplateUtilities(serviceContextPath));
		clientScheduler.setStandardPackageProcessor(standardPackageProcessor);
		Boolean isWebApp = null;
		try {
			//isWebApp = CommonUtils.isWebApp(deploymentType);
			isWebApp = true; 
			if (Constants.Config.DEPLOYMENT_TYPE_HYBRID.equals(deploymentType)) {
				isWebApp = false;
				clientScheduler.setWebApp(isWebApp); 
				boolean schedulerDefault = Boolean.parseBoolean(schedulerDefaultStart);
				if ( schedulerDefault ) {
					System.out.println("Default Scheduler started");
					clientScheduler.startScheduler();
				} else {
					System.out.println("Default Scheduler not started");
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clientScheduler;
	}
	
	@Bean
	public Docket controllerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.newArrayList(apiKey()));
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "JSESSIONID", "header");
	}
	

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("TITLE")
            .description("DESCRIPTION")
            .version("VERSION")
            .termsOfServiceUrl("http://terms-of-services.url")
            .license("LICENSE")
            .licenseUrl("http://url-to-license.com")
            .build();
    }
	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	        .securityReferences(defaultAuth())
	        .forPaths(PathSelectors.regex("/anyPath.*"))
	        .build();
	  }
 
	List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScope
	        = new AuthorizationScope("global", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	    authorizationScopes[0] = authorizationScope;
	    return Lists.newArrayList(
	        new SecurityReference("AUTHORIZATION", authorizationScopes));
	  }

	@Bean
	public RestTemplateUtilities dataValidationServicesRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin");
	}
	
	@Bean
	public RestTemplateUtilities aiRestTemplateUtilities(@Value("${service.contextPath:}") String serviceContextPath) {
		return new RestTemplateUtilities(serviceContextPath , Constants.AnvizentWSURL.ANVIZENT_SERVICES_BASE_URL  + "/aiJobs");
	}
	
	
}
