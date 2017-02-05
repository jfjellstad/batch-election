package org.fjellstad;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

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
		if (applicationArguments.containsOption("server")) {
			MDC.put("node", "server");
			logger.info("Starting server at {}", server.getURL());
			server.start();
		} else if (applicationArguments.containsOption("node")) {
			List<String> options = applicationArguments.getOptionValues("node");
			String node = "node" + nodeId;
			if (!options.isEmpty()) {
				node = "node" + options.get(0);
			}
			MDC.put("node", node);
			logger.info("Starting {}", node);
		}
	}
}
