package home.project.am;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EtfblIpSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtfblIpSpringApplication.class, args);
	}

}
