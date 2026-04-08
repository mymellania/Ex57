package com.example.ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo4Application {

	public static void main(String[] args) {

		SpringApplication.run(Demo4Application.class, args);
		System.out.println("푸하하하abcd");
		System.out.println("abcdefg".contains("bc"));
//		System.out.println(null.contains("bc")); // 에러
	}

}
