package gov.nyc.doitt.casematters.submitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubmitterSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubmitterSpringBootApplication.class, args);
	}
}
