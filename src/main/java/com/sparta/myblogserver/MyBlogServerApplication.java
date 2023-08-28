package com.sparta.myblogserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyBlogServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlogServerApplication.class, args);
	}

}
