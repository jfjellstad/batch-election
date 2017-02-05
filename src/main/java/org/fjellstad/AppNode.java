package org.fjellstad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("node")
public class AppNode implements ApplicationRunner {
	private static int nodeId = 0;

	private Logger logger = LoggerFactory.getLogger(AppNode.class);

	public AppNode() {
		nodeId++;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> options = args.getOptionValues("node");
		String node = "node" + nodeId;
		if (!options.isEmpty()) {
			node = "node" + options.get(0);
		}
		MDC.put("node", node);
		logger.info("Starting {}", node);
	}
}
