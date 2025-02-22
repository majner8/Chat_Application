package app.start;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"app","chat"})
@EntityScan(basePackages={"app","chat"})
@EnableJpaRepositories(basePackages = {"chat","app"})
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages= {"chat","app"})
public class Start {
     static final Logger logger = LogManager.getLogger(Start.class);

    public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
	}

}
