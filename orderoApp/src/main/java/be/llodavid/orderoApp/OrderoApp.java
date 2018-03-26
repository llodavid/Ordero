package be.llodavid.orderoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = {"be.llodavid"})
public class OrderoApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderoApp.class, args);
    }

}
