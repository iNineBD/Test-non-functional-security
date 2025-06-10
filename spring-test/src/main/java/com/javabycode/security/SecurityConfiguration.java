package com.javabycode.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	private static final String REALM = "MY_BASIC_AUTHENTICATION";

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/hello").permitAll()
						.requestMatchers("/admin").hasRole("ADMIN")
						.requestMatchers("/fruits/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.httpBasic(httpBasic -> httpBasic
						.realmName(REALM)
						.authenticationEntryPoint(new MyBasicAuthenticationEntryPoint())
				)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		return http.build();
	}

	// Defina o AuthenticationManager com usuários em memória
	@Bean
	public org.springframework.security.core.userdetails.UserDetailsService users() {
		var userDetailsService = new org.springframework.security.provisioning.InMemoryUserDetailsManager();

		var user1 = org.springframework.security.core.userdetails.User
				.withUsername("javabycode")
				.password("{noop}123456") // {noop} significa senha em texto simples
				.roles("USER", "ADMIN") // Adiciona o papel ADMIN
				.build();

		var admin = org.springframework.security.core.userdetails.User
				.withUsername("admin")
				.password("{noop}admin123")
				.roles("ADMIN")
				.build();

		userDetailsService.createUser(user1);
		userDetailsService.createUser(admin);

		return userDetailsService;
	}

}
