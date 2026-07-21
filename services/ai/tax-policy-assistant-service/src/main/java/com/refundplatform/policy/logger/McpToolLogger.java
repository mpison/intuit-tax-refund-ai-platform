package com.refundplatform.policy.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "mcp.logging.enabled", havingValue = "true", matchIfMissing = true)
public class McpToolLogger implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(McpToolLogger.class);

	private final ToolCallbackProvider toolProvider;

	public McpToolLogger(ToolCallbackProvider toolProvider) {
		this.toolProvider = toolProvider;
	}

	@Override
	public void run(String... args) {
		try {
			ToolCallback[] callbacks = toolProvider.getToolCallbacks();
			
			logger.info("========== Registered MCP Tools ==========");
			
			if (callbacks == null || callbacks.length == 0) {
				logger.info("No MCP tools are currently registered.");
			} else {
				for (ToolCallback callback : callbacks) {
					if (callback != null && callback.getToolDefinition() != null) {
						logger.info("Tool: {}", callback.getToolDefinition().name());
					}
				}
				logger.info("Total MCP tools: {}", callbacks.length);
			}
			
			logger.info("==========================================");
		} catch (Exception e) {
			logger.error("Error while logging MCP tools: {}", e.getMessage());
		}
	}
}