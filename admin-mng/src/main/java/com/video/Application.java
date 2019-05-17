package com.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages="com.video.mapper")
@ComponentScan(basePackages= {"com.video", "idworker"})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
