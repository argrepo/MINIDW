package com.datamodel.anvizent.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.interceptor.AnvizentAuthenticationInterceptor;
import com.datamodel.anvizent.interceptor.DataApisAuthenticationInterceptor;
import com.datamodel.anvizent.security.UserNotAuthenticatedException;
import com.datamodel.anvizent.spring.TypeConverter.StringToIndustryConverter;
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
@ComponentScan({"com.datamodel.anvizent", "com.anvizent.minidw"})
public class WebConfig extends WebMvcConfigurerAdapter {
	protected final Log log = LogFactory.getLog(WebConfig.class);
	private static final String DEFAULT_ENCODING = "UTF-8";

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
		// disabled to handle 404 exceptions
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(Constants.Config.RESOURCES + "**").addResourceLocations(Constants.Config.RESOURCES).setCachePeriod(432000); // 5 days
	
	    registry.addResourceHandler("swagger-ui.html") .addResourceLocations("classpath:/META-INF/resources/");
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

		/**
		 * intercept every url calling rest service to verify the client
		 * authentication
		 */
		registry.addInterceptor(anvizentAuthenticationInterceptor()).addPathPatterns("/anvizentWServices/**",
				"/anvizentCommonWServices/**", "/adminWServices/**");
		registry.addInterceptor(dataApisAuthenticationInterceptor()).addPathPatterns("/dataapi/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SitePreferenceHandlerMethodArgumentResolver());
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
	}

	@Bean
	public AnvizentAuthenticationInterceptor anvizentAuthenticationInterceptor() {
		return new AnvizentAuthenticationInterceptor();
	}
	
	@Bean
	public DataApisAuthenticationInterceptor dataApisAuthenticationInterceptor() {
		return new DataApisAuthenticationInterceptor();
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setCookieHttpOnly(true);
		localeResolver.setCookieSecure(true);
		return localeResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		return viewResolver;
	}

	@Bean
	public MessageSource messageSource() {
		WildcardReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();

		/*
		 * Moved the messages folder to resources instead of WEB-INF because
		 * PathMatchingResourcePatternResolver was trying to look for the
		 * messages folder in /WEB-INF/classes instead of /WEB-INF
		 */
		messageSource.setBasenames(new String[] { "classpath*:/messages/serviceMessages.properties" });
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(-1);
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
	public MultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		return mr;
	}

	@Bean
	public ServletContextAttributeExporter servletContextAttributeExporter() {
		ServletContextAttributeExporter servletContext = new ServletContextAttributeExporter();

		Map<String, Object> map = new HashMap<String, Object>();
		servletContext.setAttributes(map);
		return servletContext;
	}

	@Bean
	public Docket controllerApi() {
				return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(apiKeys());
	}

	private List<ApiKey> apiKeys() {
		List<ApiKey> apiKeys = new ArrayList<>();		
		apiKeys.add(new ApiKey("Authorization-Code", "X-Authentication-code", "header"));
		apiKeys.add(new ApiKey("Header-Client", "clientId", "header"));
		apiKeys.add(new ApiKey("Header-User-Client", "clientIdOriginal", "header"));
		return apiKeys;
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
	        .forPaths(PathSelectors.regex("/.*"))
	        .build();
	  }
 
	List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScope
	        = new AuthorizationScope("global", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	    authorizationScopes[0] = authorizationScope;
	    List<SecurityReference> securityRefs = new ArrayList<>();
	    securityRefs.add(new SecurityReference("Authorization-Code", authorizationScopes));
	    securityRefs.add(new SecurityReference("Header-Client", authorizationScopes));
	    securityRefs.add(new SecurityReference("Header-User-Client", authorizationScopes));
	    return securityRefs;
	  }

}
