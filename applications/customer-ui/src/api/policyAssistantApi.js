const assistantApiUrl =
    import.meta.env.VITE_POLICY_ASSISTANT_API_URL
    || "http://localhost:8060";

export async function askPolicyAssistant(
    conversationId,
    question,
    accessToken
) {

    if (!accessToken) {
        throw new Error(
            "No access token is available. Please sign in again."
        );
    }

    const response =
        await fetch(
            `${assistantApiUrl}/api/v1/assistant/chat`,
            {
                method:
                    "POST",

                headers:
                    {
                        "Content-Type":
                            "application/json",

                        Authorization:
                            `Bearer ${accessToken}`
                    },

                body:
                    JSON.stringify(
                        {
                            conversationId,
                            question
                        }
                    )
            }
        );

    if (!response.ok) {

        let errorMessage =
            `Policy Assistant failed with status ${response.status}`;

        try {
            const errorBody =
                await response.json();

            errorMessage =
                errorBody.message
                || errorBody.error
                || errorMessage;
        }
        catch {
            // Keep the default message when the response is not JSON.
        }

        if (response.status === 401) {
            throw new Error(
                "Your session has expired. Please sign in again."
            );
        }

        throw new Error(
            errorMessage
        );
    }

    return response.json();
}