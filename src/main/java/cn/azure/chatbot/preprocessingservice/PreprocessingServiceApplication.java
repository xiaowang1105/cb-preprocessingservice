package cn.azure.chatbot.preprocessingservice;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@Configuration
@EnableAutoConfiguration
public class PreprocessingServiceApplication {
    // Enable CORS support
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOrigins("*");
            }
        };
    }

    // For healthy check
    @GetMapping("/")
    @ApiOperation("Returns string OK, for health check.")
    public String ping() {
        return "OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(PreprocessingServiceApplication.class, args);
    }
}
