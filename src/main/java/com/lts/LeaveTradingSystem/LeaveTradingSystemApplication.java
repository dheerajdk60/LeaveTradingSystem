package com.lts.LeaveTradingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeaveTradingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveTradingSystemApplication.class, args);
	}

	@Bean
	public StringUtil stringUtil()
	{
		return new StringUtil();
	}
}
