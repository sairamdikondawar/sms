package com.sms.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.sms.demo.service.AccountService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	AccountService accountService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(accountService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//				.authorizeRequests().anyRequest().fullyAuthenticated().and().httpBasic();
//		
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/swagger-ui/**", "/tagnos-openapi/**", "/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()
		.antMatchers("/swagger-ui.html").permitAll()
		.anyRequest().fullyAuthenticated().and().httpBasic();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v3/api-docs", "/configuration/ui","/swagger-ui/**", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**");
	}

}
