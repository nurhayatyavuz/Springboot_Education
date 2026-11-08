package com.exampla.surucu;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SurucuApplication {

	public static void main(String[] args) {

        SpringApplication.run(SurucuApplication.class, args);
	}
    //alttaki biçimde de çalışacaktı
//    @Bean
//    public CommandLineRunner commmandLineRunner(ApplicationContext ctx){
//        return args -> {
//            RestTemplate restTemplate=new RestTemplate();
//        };
//    }


}
