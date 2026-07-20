package com.refundplatform.policy.ai.orchestration;

import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.Optional;

public interface ToolRegistry {

    List<ToolMetadata> listTools();

    List<ToolCallback> toolCallbacks();

    Optional<ToolCallback> findByName(String toolName);

    boolean hasTool(String toolName);
}
