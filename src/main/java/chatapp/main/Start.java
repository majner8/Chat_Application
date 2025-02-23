package chatapp.main;

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
@ComponentScan(basePackages = "chatapp")
@EntityScan(basePackages="chatapp")
@EnableJpaRepositories(basePackages = "chatapp")
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages= "chatapp")
public class Start {
     static final Logger logger = LogManager.getLogger(Start.class);

    public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
	}

}
