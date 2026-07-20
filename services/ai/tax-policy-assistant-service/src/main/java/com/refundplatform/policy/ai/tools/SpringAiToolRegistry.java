package com.refundplatform.policy.ai.tools;

import com.refundplatform.policy.ai.orchestration.ToolMetadata;
import com.refundplatform.policy.ai.orchestration.ToolRegistry;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class SpringAiToolRegistry implements ToolRegistry {

    private final List<ToolCallbackProvider> providers;

    public SpringAiToolRegistry(
            ObjectProvider<ToolCallbackProvider> providerObjectProvider) {

        this.providers = providerObjectProvider
                .orderedStream()
                .toList();
    }

    @Override
    public List<ToolMetadata> listTools() {

        return callbacksByName()
                .values()
                .stream()
                .map(callback -> new ToolMetadata(
                        callback.getToolDefinition().name(),
                        callback.getToolDefinition().description(),
                        callback.getToolDefinition().inputSchema(),
                        callback.getClass().getName()
                ))
                .sorted(Comparator.comparing(ToolMetadata::name))
                .toList();
    }

    @Override
    public List<ToolCallback> toolCallbacks() {

        return callbacksByName()
                .values()
                .stream()
                .toList();
    }

    @Override
    public Optional<ToolCallback> findByName(
            String toolName) {

        if (toolName == null || toolName.isBlank()) {
            return Optional.empty();
        }

        Map<String, ToolCallback> callbacks = callbacksByName();

        ToolCallback exactMatch = callbacks.get(toolName);

        if (exactMatch != null) {
            return Optional.of(exactMatch);
        }

        String normalizedRequestedName =
                toolName.toLowerCase(Locale.ROOT);

        List<ToolCallback> suffixMatches =
                callbacks.entrySet()
                        .stream()
                        .filter(entry ->
                                entry.getKey()
                                        .toLowerCase(Locale.ROOT)
                                        .endsWith(normalizedRequestedName)
                        )
                        .map(Map.Entry::getValue)
                        .toList();

        return suffixMatches.size() == 1
                ? Optional.of(suffixMatches.getFirst())
                : Optional.empty();
    }

    @Override
    public boolean hasTool(
            String toolName) {

        return findByName(toolName).isPresent();
    }

    private Map<String, ToolCallback> callbacksByName() {

        Map<String, ToolCallback> callbacks =
                new LinkedHashMap<>();

        for (ToolCallbackProvider provider : providers) {
            ToolCallback[] providerCallbacks =
                    provider.getToolCallbacks();

            if (providerCallbacks == null) {
                continue;
            }

            Arrays.stream(providerCallbacks)
                    .forEach(callback ->
                            callbacks.putIfAbsent(
                                    callback.getToolDefinition().name(),
                                    callback
                            )
                    );
        }

        return callbacks;
    }
}
