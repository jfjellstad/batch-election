package org.fjellstad;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Profile("server")
public class AppServer implements ApplicationRunner {
	private final Logger logger = LoggerFactory.getLogger(AppServer.class);
	private final Server server;

	@Inject
	public AppServer(Server server) {
		this.server = server;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		MDC.put("node", "server");
		logger.info("Starting server at {}", server.getURL());
		server.start();
	}
}
