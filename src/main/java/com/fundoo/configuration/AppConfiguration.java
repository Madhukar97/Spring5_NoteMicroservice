package com.fundoo.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import com.fundoo.service.EmailService;
import com.fundoo.util.Response;

@Configuration
public class AppConfiguration {


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Bean
	public Response response() {
		return new Response();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public EmailService emailService() {
		return new EmailService();
	}
	
	@Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
