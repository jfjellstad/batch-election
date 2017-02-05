package org.fjellstad;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AppServer implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(AppServer.class);
	private static int nodeId = 0;
	private final Server server;
	private final ApplicationArguments applicationArguments;

	@Inject
	public AppServer(Server server, ApplicationArguments applicationArguments) {
		this.server = server;
		this.applicationArguments = applicationArguments;
		nodeId++;
	}

	@Override
	public void run(String... args) throws Exception {
		if (applicationArguments.getNonOptionArgs().contains("server")) {
			logger.info("Starting server {}", server.getURL());
			server.start();
		} else {
			logger.info("Starting node{}", nodeId);
		}
	}
}
