const refundApiUrl =
    import.meta.env.VITE_REFUND_API_URL
    || "http://localhost:8080";

export async function getCurrentUserProfile(
    accessToken
) {
    const response =
        await fetch(
            `${refundApiUrl}/api/v1/users/me`,
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            }
        );

    if (!response.ok) {
        throw new Error(
            `Profile request failed with status ${response.status}`
        );
    }

    return response.json();
}

export async function updateCurrentUserProfile(
    accessToken,
    profile
) {
    const response =
        await fetch(
            `${refundApiUrl}/api/v1/users/me`,
            {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(profile)
            }
        );

    if (!response.ok) {
        throw new Error(
            `Profile update failed with status ${response.status}`
        );
    }

    return response.json();
}
