package com.sds.hospapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling	//cron 걸려면 이거 써야된다.
@SpringBootApplication
public class HospappApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospappApplication.class, args);
	}

}
