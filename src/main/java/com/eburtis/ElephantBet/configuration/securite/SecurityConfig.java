package com.eburtis.ElephantBet.configuration.securite;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtRequestFilter jwtRequestFilter;

	public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors()
				.and()
				.csrf()
				.disable().authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/api/utilisateurs").hasAnyAuthority("ADMIN")
				.antMatchers("/api-docs/**").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/api/securite/auth").permitAll()
				.antMatchers("/api/ticket/telecharger").permitAll()
				.antMatchers("/fichiers/**").permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
