package org.fjellstad;

import org.fjellstad.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.StandardEnvironment;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
    	SpringApplication application = new SpringApplication(AppConfig.class);
    	if (args.length > 0) {
    		logger.info("arguments {}", args);
		    StandardEnvironment env = new StandardEnvironment();
    		if (args[0].contains("server")) {
    			env.setActiveProfiles("server");
		    } else if (args[0].contains("node")) {
    			env.setActiveProfiles("node");
		    }
		    application.setEnvironment(env);
    		application.run(args);
	    } else {
    		logger.error("Invalid option");
	    }
    }
}
