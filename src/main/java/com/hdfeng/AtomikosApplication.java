package com.hdfeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hdfeng.config.CustomerDatasourceProperties;
import com.hdfeng.config.OrderDatasourceProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { CustomerDatasourceProperties.class,OrderDatasourceProperties.class })
public class AtomikosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtomikosApplication.class, args);
	}

}
