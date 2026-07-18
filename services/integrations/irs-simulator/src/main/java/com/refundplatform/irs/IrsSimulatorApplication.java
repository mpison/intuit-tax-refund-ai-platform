package com.refundplatform.irs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrsSimulatorApplication {

    public static void main(
            String[] arguments) {

        SpringApplication.run(
                IrsSimulatorApplication.class,
                arguments
        );
    }
}
