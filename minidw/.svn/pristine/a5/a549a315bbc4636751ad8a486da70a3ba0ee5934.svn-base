package com.datamodel.anvizent.spring;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.datamodel.anvizent.interceptor.CustomAccessDeniedHandler;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		RequestMatcher csrfRequestMatcher = new RequestMatcher() {

			// Always allow the HTTP GET method
			private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

			// Disable CSFR protection on the following urls:
			private AntPathRequestMatcher[] requestMatchers = { new AntPathRequestMatcher("/securityCheck"), new AntPathRequestMatcher("/sessionExpired") };

			@Override
			public boolean matches(HttpServletRequest request) {

				// Skip allowed methods
				if (allowedMethods.matcher(request.getMethod()).matches()) {
					return false;
				}
				// If the request match one url the CSFR protection will be
				// disabled
				for (AntPathRequestMatcher rm : requestMatchers) {
					if (rm.matches(request)) {
						return false;

					}
				}
				return true;
			}
		};

		http.headers().frameOptions().xssProtection().contentTypeOptions().and().csrf().requireCsrfProtectionMatcher(csrfRequestMatcher);
		http.headers().cacheControl().and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler()).accessDeniedPage("/errors/403");

	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
}
