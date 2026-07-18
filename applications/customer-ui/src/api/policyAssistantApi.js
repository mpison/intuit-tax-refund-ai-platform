const assistantApiUrl =
    import.meta.env.VITE_POLICY_ASSISTANT_API_URL
    || "http://localhost:8060";

export async function askPolicyAssistant(
    conversationId,
    question
) {

    const response =
        await fetch(
            `${assistantApiUrl}/api/v1/assistant/chat`,
            {
                method:
                    "POST",

                headers:
                    {
                        "Content-Type":
                            "application/json"
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

        throw new Error(
            `Policy Assistant failed with status ${response.status}`
        );
    }

    return response.json();
}
