package com.fokantech.media_stream_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.fokantech.media_stream_service.mapper")
public class MediaStreamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaStreamServiceApplication.class, args);
	}

}
