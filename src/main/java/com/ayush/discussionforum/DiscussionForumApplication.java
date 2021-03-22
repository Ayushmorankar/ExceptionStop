package com.ayush.discussionforum;

import com.ayush.discussionforum.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class DiscussionForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscussionForumApplication.class, args);
    }

}
