package org.fjellstad;

import org.fjellstad.config.AppConfig;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;

public class App {
    public static void main( String[] args ) {
        SpringApplication.run(AppConfig.class, args);
        MDC.remove("node");
    }
}
