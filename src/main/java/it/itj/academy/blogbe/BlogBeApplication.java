package it.itj.academy.blogbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BlogBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogBeApplication.class, args);
    }
}
