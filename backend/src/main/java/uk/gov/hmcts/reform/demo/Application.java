package uk.gov.hmcts.reform.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.hmcts.reform.demo.services.OpenAIService;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.demo", "uk.gov.hmcts.reform.exceptions" })
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableJpaRepositories(basePackages = "uk.gov.hmcts.reform.demo.repositories")
@EntityScan(basePackages = "uk.gov.hmcts.reform.demo")

public class Application {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*") // Restrict to specific origin
                    .allowedMethods("*") // Specify allowed methods
                    .allowedHeaders("*") // Allow all headers
                //.allowCredentials(true) // Allow credentials like cookies
                ;
            }
        };
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //@Override
    //public void run(String... args) throws Exception {
        //openAIService.runTest();
    //}
}
