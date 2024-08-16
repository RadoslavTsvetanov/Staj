package uk.gov.hmcts.reform.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.gov.hmcts.reform.demo.services.OpenAIService;

@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.demo" })
@EnableJpaRepositories(basePackages = "uk.gov.hmcts.reform.demo.repositories")
@EntityScan(basePackages = "uk.gov.hmcts.reform.demo")
//public class Application {

 //   public static void main(final String[] args) {
   //     SpringApplication.run(Application.class, args);
   // }
//}

public class Application implements CommandLineRunner {
    @Autowired
    private OpenAIService openAIService;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        openAIService.runTest();
    }
}
