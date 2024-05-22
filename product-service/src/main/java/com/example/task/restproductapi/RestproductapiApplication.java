package com.example.task.restproductapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RestproductapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestproductapiApplication.class, args);
	}

}
