import React, { useEffect, useRef, useState } from "react";
import { askPolicyAssistant } from "../api/policyAssistantApi";

const storageKey = "refund-platform-policy-chat-history";

export default function PolicyAssistant() {
    const [isOpen, setIsOpen] = useState(false);
    const [question, setQuestion] = useState("");
    const [messages, setMessages] = useState(loadMessages);
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const messageListRef = useRef(null);

    useEffect(() => {
        localStorage.setItem(storageKey, JSON.stringify(messages));
    }, [messages]);

    useEffect(() => {
        if (isOpen && messageListRef.current) {
            requestAnimationFrame(() => {
                messageListRef.current.scrollTop =
                    messageListRef.current.scrollHeight;
            });
        }
    }, [messages, isOpen]);

    async function submitQuestion(event) {
        event.preventDefault();

        const trimmedQuestion = question.trim();
        if (!trimmedQuestion || isLoading) {
            return;
        }

        setMessages(current => [
            ...current,
            createMessage("user", trimmedQuestion, [])
        ]);

        setQuestion("");
        setErrorMessage("");
        setIsLoading(true);

        try {
            const response = await askPolicyAssistant(
                "ui-demo-conversation",
                trimmedQuestion
            );

            setMessages(current => [
                ...current,
                createMessage(
                    "assistant",
                    response.answer,
                    response.sources || []
                )
            ]);
        } catch (error) {
            setErrorMessage(error.message);
            setMessages(current => [
                ...current,
                createMessage(
                    "system",
                    "The assistant could not complete the request. Please try again.",
                    []
                )
            ]);
        } finally {
            setIsLoading(false);
        }
    }

    function clearHistory() {
        setMessages([welcomeMessage()]);
        setErrorMessage("");
    }

    return (
        <div className={isOpen ? "floatingChatbot open" : "floatingChatbot"}>
            {!isOpen && (
                <button
                    className="chatLauncher"
                    onClick={() => setIsOpen(true)}
                    aria-label="Open Tax Policy Assistant"
                >
                    <span className="chatLauncherIcon" aria-hidden="true">💬</span>
                    <span className="chatLauncherText">Ask Tax Assistant</span>
                </button>
            )}

            {isOpen && (
                <section className="chatWindow" aria-label="Tax Policy Assistant">
                    <header className="chatHeader">
                        <div>
                            <h2>Tax Policy Assistant</h2>
                            <p>General refund policy guidance</p>
                        </div>

                        <div className="chatHeaderActions">
                            <button
                                type="button"
                                className="chatIconButton"
                                onClick={clearHistory}
                            >
                                Clear
                            </button>

                            <button
                                type="button"
                                className="chatIconButton"
                                onClick={() => setIsOpen(false)}
                                aria-label="Collapse chat"
                            >
                                —
                            </button>
                        </div>
                    </header>

                    <div className="chatMessages" ref={messageListRef}>
                        {messages.map(message => (
                            <article
                                key={message.id}
                                className={`chatMessage ${message.role}`}
                            >
                                <div className="chatMessageBubble">
                                    <p>{message.text}</p>

                                    {message.sources.length > 0 && (
                                        <div className="chatSources">
                                            <span>Sources</span>
                                            <ul>
                                                {message.sources.map(source => (
                                                    <li key={source}>{source}</li>
                                                ))}
                                            </ul>
                                        </div>
                                    )}

                                    <time>{formatTime(message.createdAt)}</time>
                                </div>
                            </article>
                        ))}

                        {isLoading && (
                            <article className="chatMessage assistant">
                                <div className="chatMessageBubble typingBubble">
                                    <span></span>
                                    <span></span>
                                    <span></span>
                                </div>
                            </article>
                        )}
                    </div>

                    {errorMessage && (
                        <div className="chatError">{errorMessage}</div>
                    )}

                    <form className="chatComposer" onSubmit={submitQuestion}>
                        <textarea
                            rows="2"
                            value={question}
                            placeholder="Ask about refund processing..."
                            onChange={event => setQuestion(event.target.value)}
                            onKeyDown={event => {
                                if (event.key === "Enter" && !event.shiftKey) {
                                    event.preventDefault();
                                    submitQuestion(event);
                                }
                            }}
                        />

                        <button
                            type="submit"
                            disabled={isLoading || !question.trim()}
                        >
                            Send
                        </button>
                    </form>
                </section>
            )}
        </div>
    );
}

function loadMessages() {
    try {
        const stored = localStorage.getItem(storageKey);
        if (!stored) {
            return [welcomeMessage()];
        }

        const parsed = JSON.parse(stored);
        return Array.isArray(parsed) ? parsed : [welcomeMessage()];
    } catch {
        return [welcomeMessage()];
    }
}

function welcomeMessage() {
    return createMessage(
        "assistant",
        "Hi! Ask me a general question about tax refund processing policies.",
        []
    );
}

function createMessage(role, text, sources) {
    return {
        id: `${Date.now()}-${Math.random()}`,
        role,
        text,
        sources,
        createdAt: new Date().toISOString()
    };
}

function formatTime(timestamp) {
    return new Intl.DateTimeFormat("en-US", {
        hour: "numeric",
        minute: "2-digit"
    }).format(new Date(timestamp));
}
