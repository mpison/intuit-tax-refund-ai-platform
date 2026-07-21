package com.refundplatform.policy.ai.tools;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicySearchTool {

    private static final int DEFAULT_TOP_K =
            4;

    private static final double DEFAULT_SIMILARITY_THRESHOLD =
            0.55;

    private final VectorStore vectorStore;

    public PolicySearchTool(
            VectorStore vectorStore) {

        this.vectorStore =
                vectorStore;
    }

    @Tool(
            name = "search_tax_policy",
            description = """
                    Search the uploaded tax-refund policy knowledge base.

                    Use this tool for general policy questions, standard
                    processing windows, definitions, eligibility rules,
                    delayed-refund explanations, amended-return guidance,
                    and other information that should be grounded in the
                    uploaded policy documents.

                    Do not use this tool to invent customer-specific account
                    facts. Customer facts must come from MCP account tools.
                    """
    )
    public PolicySearchResult searchTaxPolicy(
            @ToolParam(
                    description = """
                            A focused natural-language search query describing
                            the policy rule, processing window, definition, or
                            explanation needed to answer the user's question.
                            """,
                    required = true
            )
            String query) {

        if (
                query == null
                || query.isBlank()
        ) {

            return new PolicySearchResult(
                    false,
                    query,
                    List.of(),
                    "A non-empty policy search query is required."
            );
        }

        List<Document> documents =
                vectorStore.similaritySearch(
                        SearchRequest
                                .builder()
                                .query(
                                        query
                                )
                                .topK(
                                        DEFAULT_TOP_K
                                )
                                .similarityThreshold(
                                        DEFAULT_SIMILARITY_THRESHOLD
                                )
                                .build()
                );

        if (
                documents == null
                || documents.isEmpty()
        ) {

            return new PolicySearchResult(
                    false,
                    query,
                    List.of(),
                    "No sufficiently similar policy content was found."
            );
        }

        List<PolicySearchMatch> matches =
                documents
                        .stream()
                        .map(
                                this::toMatch
                        )
                        .toList();

        return new PolicySearchResult(
                true,
                query,
                matches,
                "Policy content was retrieved successfully."
        );
    }

    private PolicySearchMatch toMatch(
            Document document) {

        String title =
                String.valueOf(
                        document
                                .getMetadata()
                                .getOrDefault(
                                        "title",
                                        "Policy"
                                )
                );

        String source =
                String.valueOf(
                        document
                                .getMetadata()
                                .getOrDefault(
                                        "source",
                                        title
                                )
                );

        String content =
                document.getText() == null
                        ? ""
                        : document.getText();

        return new PolicySearchMatch(
                title,
                source,
                content
        );
    }

    public record PolicySearchResult(
            boolean found,
            String query,
            List<PolicySearchMatch> matches,
            String message
    ) {

        public PolicySearchResult {

            matches =
                    matches == null
                            ? List.of()
                            : List.copyOf(
                                    matches
                            );
        }
    }

    public record PolicySearchMatch(
            String title,
            String source,
            String content
    ) {
    }
}
