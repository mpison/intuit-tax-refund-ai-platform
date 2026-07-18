export async function getLatestRefund(
    accessToken
) {

    const apiUrl =
        import.meta.env.VITE_REFUND_API_URL
        || "http://localhost:8080";

    const response =
        await fetch(
            `${apiUrl}/api/v1/refunds/latest`,
            {
                headers:
                    {
                        Authorization:
                            `Bearer ${accessToken}`
                    }
            }
        );

    if (!response.ok) {

        throw new Error(
            `Refund API failed with status ${response.status}`
        );
    }

    return response.json();
}
