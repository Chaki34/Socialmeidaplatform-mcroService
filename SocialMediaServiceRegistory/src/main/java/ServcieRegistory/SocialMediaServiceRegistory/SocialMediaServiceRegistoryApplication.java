package ServcieRegistory.SocialMediaServiceRegistory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SocialMediaServiceRegistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaServiceRegistoryApplication.class, args);
	}

}
