import {
    useCallback
} from "react";

import keycloak from "../keycloak";

import {
    sendAssistantMessage
} from "../api/policyAssistantApi";

export function usePolicyAssistant() {
    const askAssistant =
        useCallback(
            async ({
                conversationId,
                question
            }) => {
                if (!keycloak.authenticated) {
                    await keycloak.login();
                    throw new Error(
                        "Authentication is required."
                    );
                }

                try {
                    await keycloak.updateToken(30);
                }
                catch {
                    await keycloak.login();
                    throw new Error(
                        "Your session expired. Please sign in again."
                    );
                }

                return sendAssistantMessage({
                    conversationId,
                    question,
                    accessToken: keycloak.token
                });
            },
            []
        );

    return {
        askAssistant
    };
}
