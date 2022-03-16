package com.crazyjava.diskmanager;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class DiskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiskManagerApplication.class, args);
	}

	@Bean
    public Executor taskExecutor() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setCorePoolSize(10);
    	executor.setMaxPoolSize(20);
    	executor.setQueueCapacity(500);
    	executor.setThreadNamePrefix("HttpThread-");
    	executor.initialize();
    	return executor;
    }
}
