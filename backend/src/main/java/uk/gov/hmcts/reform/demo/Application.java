package uk.gov.hmcts.reform.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.demo" })
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableJpaRepositories(basePackages = "uk.gov.hmcts.reform.demo.repositories")
@EntityScan(basePackages = "uk.gov.hmcts.reform.demo")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
