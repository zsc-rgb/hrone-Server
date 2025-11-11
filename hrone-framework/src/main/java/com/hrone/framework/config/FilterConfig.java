package com.hrone.framework.config;

import com.hrone.framework.security.jwt.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器注册（注册顺序：越小越先执行）
 */
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(filter);
		bean.addUrlPatterns("/*");
		bean.setName("jwtAuthenticationFilter");
		bean.setOrder(10);
		return bean;
	}
}


