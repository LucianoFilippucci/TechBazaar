package it.lucianofilippucci.university.techbazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechBazaarApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechBazaarApplication.class, args);
        System.out.println("CIAO");
    }

}
