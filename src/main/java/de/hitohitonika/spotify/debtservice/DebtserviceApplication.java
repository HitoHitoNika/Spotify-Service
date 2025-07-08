package de.hitohitonika.spotify.debtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DebtserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebtserviceApplication.class, args);
    }

}
