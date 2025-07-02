package com.example.xiaohongshu_ai_helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class XiaohongshuAiHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiaohongshuAiHelperApplication.class, args);
	}

}
