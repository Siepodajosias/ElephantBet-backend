package com.eburtis.ElephantBet.configuration.securite;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Value("${client.base_url.local}")
	private String CLIENT_LOCAL;

	@Value("${client.base_url.online}")
	private String CLIENT_ONLINE;

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		corsRegistry.addMapping("/**")
				.allowedOrigins(CLIENT_LOCAL, CLIENT_ONLINE)
				.allowedMethods("*")
				.maxAge(3600L)
				.allowedHeaders("*")
				.exposedHeaders("Authorization")
				.allowCredentials(true);
	}
}
