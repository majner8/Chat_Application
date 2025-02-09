package app.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"app","chat"})
@EntityScan(basePackages={"app","chat"})
@EnableJpaRepositories(basePackages = {"chat","app"})
@EnableAutoConfiguration

public class Start {

	public static void main(String[] args) {
		SpringApplication.run(Start.class, args);

	}

}
