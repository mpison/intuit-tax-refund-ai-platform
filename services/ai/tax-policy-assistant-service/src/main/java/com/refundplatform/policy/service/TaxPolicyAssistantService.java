package com.refundplatform.policy.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.refundplatform.policy.dto.AssistantRequest;
import com.refundplatform.policy.dto.AssistantResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxPolicyAssistantService {

    private static final String SYSTEM_PROMPT =
            """
            You are a tax refund policy assistant.

            Use only the supplied policy context.
            Do not claim access to the user's tax account.
            Do not provide legal or tax advice.
            Clearly state when the context is insufficient.
            Keep the response concise and actionable.
            """;

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    public TaxPolicyAssistantService(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore) {

        this.chatClient =
                chatClientBuilder.build();

        this.vectorStore =
                vectorStore;
    }

    public AssistantResponse answer(
            AssistantRequest request) {

        List<Document> documents =
                vectorStore.similaritySearch(
                        SearchRequest
                                .builder()
                                .query(
                                        request.question()
                                )
                                .topK(
                                        4
                                )
                                .similarityThreshold(
                                        0.55
                                )
                                .build()
                );

        String context =
                documents
                        .stream()
                        .map(
                                document ->
                                        """
                                        Source: %s
                                        %s
                                        """
                                        .formatted(
                                                document
                                                        .getMetadata()
                                                        .getOrDefault(
                                                                "title",
                                                                "Policy"
                                                        ),
                                                document.getText()
                                        )
                        )
                        .collect(
                                Collectors.joining(
                                        "\n\n"
                                )
                        );

        String answer =
                chatClient
                        .prompt()
                        .system(
                                SYSTEM_PROMPT
                        )
                        .user(
                                userPrompt(
                                        request.question(),
                                        context
                                )
                        )
                        .call()
                        .content();

        List<String> sources =
                documents
                        .stream()
                        .map(
                                document ->
                                        String.valueOf(
                                                document
                                                        .getMetadata()
                                                        .getOrDefault(
                                                                "title",
                                                                "Policy"
                                                        )
                                        )
                        )
                        .distinct()
                        .toList();

        return new AssistantResponse(
                answer,
                sources
        );
    }

    private String userPrompt(
            String question,
            String context) {

        return """
                Policy context:

                %s

                Question:

                %s

                Answer using only the policy context.
                """
                .formatted(
                        context,
                        question
                );
    }
}
