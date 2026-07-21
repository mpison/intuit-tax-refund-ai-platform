import React, {
    useEffect,
    useMemo,
    useRef,
    useState
} from "react";

import {
    keycloak
} from "../auth/keycloak";

import {
    askPolicyAssistant
} from "../api/policyAssistantApi";

const storageKeyPrefix =
    "refund-platform-policy-chat-history";

const conversationKeyPrefix =
    "refund-platform-policy-conversation-id";

export default function PolicyAssistant() {

    const userSubject =
        keycloak.tokenParsed?.sub
        || keycloak.subject
        || "anonymous";

    const userStorageKey =
        useMemo(
            () =>
                `${storageKeyPrefix}:${userSubject}`,
            [
                userSubject
            ]
        );

    const userConversationKey =
        useMemo(
            () =>
                `${conversationKeyPrefix}:${userSubject}`,
            [
                userSubject
            ]
        );

    const [
        isOpen,
        setIsOpen
    ] = useState(false);

    const [
        question,
        setQuestion
    ] = useState("");

    const [
        messages,
        setMessages
    ] = useState(
        () =>
            loadMessages(
                userStorageKey
            )
    );

    const [
        isLoading,
        setIsLoading
    ] = useState(false);

    const [
        errorMessage,
        setErrorMessage
    ] = useState("");

    const messageListRef =
        useRef(null);

    const conversationIdRef =
        useRef(
            getOrCreateConversationId(
                userConversationKey
            )
        );

    useEffect(
        () => {

            setMessages(
                loadMessages(
                    userStorageKey
                )
            );

            conversationIdRef.current =
                getOrCreateConversationId(
                    userConversationKey
                );
        },
        [
            userStorageKey,
            userConversationKey
        ]
    );

    useEffect(
        () => {

            localStorage.setItem(
                userStorageKey,
                JSON.stringify(
                    messages
                )
            );
        },
        [
            messages,
            userStorageKey
        ]
    );

    useEffect(
        () => {

            if (
                isOpen
                && messageListRef.current
            ) {

                requestAnimationFrame(
                    () => {

                        messageListRef.current.scrollTop =
                            messageListRef.current.scrollHeight;
                    }
                );
            }
        },
        [
            messages,
            isOpen
        ]
    );

    async function submitQuestion(
        event) {

        event.preventDefault();

        const trimmedQuestion =
            question.trim();

        if (
            !trimmedQuestion
            || isLoading
        ) {
            return;
        }

        setMessages(
            current => [
                ...current,
                createMessage(
                    "user",
                    trimmedQuestion,
                    []
                )
            ]
        );

        setQuestion("");
        setErrorMessage("");
        setIsLoading(true);

        try {

            await keycloak.updateToken(
                30
            );

            const response =
                await askPolicyAssistant(
                    conversationIdRef.current,
                    trimmedQuestion,
                    keycloak.token
                );

            setMessages(
                current => [
                    ...current,
                    createMessage(
                        "assistant",
                        response.answer,
                        response.sources || []
                    )
                ]
            );
        }
        catch (error) {

            setErrorMessage(
                error.message
            );

            setMessages(
                current => [
                    ...current,
                    createMessage(
                        "system",
                        "The assistant could not complete the request. Please try again.",
                        []
                    )
                ]
            );
        }
        finally {

            setIsLoading(false);
        }
    }

    function clearHistory() {

        localStorage.removeItem(
            userStorageKey
        );

        localStorage.removeItem(
            userConversationKey
        );

        conversationIdRef.current =
            createConversationId();

        localStorage.setItem(
            userConversationKey,
            conversationIdRef.current
        );

        setMessages(
            [
                welcomeMessage()
            ]
        );

        setErrorMessage("");
    }

    return (

        <div
            className={
                isOpen
                    ? "floatingChatbot open"
                    : "floatingChatbot"
            }>

            {
                !isOpen
                && (

                    <button
                        className="chatLauncher"
                        onClick={
                            () =>
                                setIsOpen(
                                    true
                                )
                        }
                        aria-label="Open Tax Policy Assistant">

                        <span
                            className="chatLauncherIcon"
                            aria-hidden="true">

                            💬

                        </span>

                        <span className="chatLauncherText">

                            Ask Tax Assistant

                        </span>

                    </button>
                )
            }

            {
                isOpen
                && (

                    <section
                        className="chatWindow"
                        aria-label="Tax Policy Assistant">

                        <header className="chatHeader">

                            <div>

                                <h2>
                                    Tax Policy Assistant
                                </h2>

                                <p>
                                    Refund status and policy guidance
                                </p>

                            </div>

                            <div className="chatHeaderActions">

                                <button
                                    type="button"
                                    className="chatIconButton"
                                    onClick={clearHistory}>

                                    Clear

                                </button>

                                <button
                                    type="button"
                                    className="chatIconButton"
                                    onClick={
                                        () =>
                                            setIsOpen(
                                                false
                                            )
                                    }
                                    aria-label="Collapse chat">

                                    —

                                </button>

                            </div>

                        </header>

                        <div
                            className="chatMessages"
                            ref={messageListRef}>

                            {
                                messages.map(
                                    message => (

                                        <article
                                            key={message.id}
                                            className={
                                                `chatMessage ${message.role}`
                                            }>

                                            <div className="chatMessageBubble">

                                                <p>
                                                    {message.text}
                                                </p>

                                                {
                                                    message.sources.length > 0
                                                    && (

                                                        <div className="chatSources">

                                                            <span>
                                                                Sources
                                                            </span>

                                                            <ul>

                                                                {
                                                                    message.sources.map(
                                                                        source => (

                                                                            <li key={source}>
                                                                                {source}
                                                                            </li>
                                                                        )
                                                                    )
                                                                }

                                                            </ul>

                                                        </div>
                                                    )
                                                }

                                                <time>
                                                    {formatTime(message.createdAt)}
                                                </time>

                                            </div>

                                        </article>
                                    )
                                )
                            }

                            {
                                isLoading
                                && (

                                    <article className="chatMessage assistant">

                                        <div className="chatMessageBubble typingBubble">

                                            <span></span>
                                            <span></span>
                                            <span></span>

                                        </div>

                                    </article>
                                )
                            }

                        </div>

                        {
                            errorMessage
                            && (

                                <div className="chatError">

                                    {errorMessage}

                                </div>
                            )
                        }

                        <form
                            className="chatComposer"
                            onSubmit={submitQuestion}>

                            <textarea
                                rows="2"
                                value={question}
                                placeholder="Ask about your refund or refund policy..."
                                onChange={
                                    event =>
                                        setQuestion(
                                            event.target.value
                                        )
                                }
                                onKeyDown={
                                    event => {

                                        if (
                                            event.key === "Enter"
                                            && !event.shiftKey
                                        ) {

                                            event.preventDefault();

                                            submitQuestion(
                                                event
                                            );
                                        }
                                    }
                                }
                            />

                            <button
                                type="submit"
                                disabled={
                                    isLoading
                                    || !question.trim()
                                }>

                                Send

                            </button>

                        </form>

                    </section>
                )
            }

        </div>
    );
}

function loadMessages(
    storageKey) {

    try {

        const stored =
            localStorage.getItem(
                storageKey
            );

        if (!stored) {
            return [
                welcomeMessage()
            ];
        }

        const parsed =
            JSON.parse(
                stored
            );

        return Array.isArray(
            parsed
        )
            ? parsed
            : [
                welcomeMessage()
            ];
    }
    catch {

        return [
            welcomeMessage()
        ];
    }
}

function getOrCreateConversationId(
    conversationKey) {

    const existingConversationId =
        localStorage.getItem(
            conversationKey
        );

    if (existingConversationId) {
        return existingConversationId;
    }

    const newConversationId =
        createConversationId();

    localStorage.setItem(
        conversationKey,
        newConversationId
    );

    return newConversationId;
}

function createConversationId() {

    return globalThis.crypto?.randomUUID?.()
        || `chat-${Date.now()}-${Math.random()}`;
}

function welcomeMessage() {

    return createMessage(
        "assistant",
        "Hi! I can help with your refund status, expected refund date, and refund policy questions.",
        []
    );
}

function createMessage(
    role,
    text,
    sources) {

    return {
        id:
            `${Date.now()}-${Math.random()}`,
        role,
        text,
        sources,
        createdAt:
            new Date().toISOString()
    };
}

function formatTime(
    timestamp) {

    return new Intl.DateTimeFormat(
        "en-US",
        {
            hour:
                "numeric",

            minute:
                "2-digit"
        }
    )
    .format(
        new Date(
            timestamp
        )
    );
}
