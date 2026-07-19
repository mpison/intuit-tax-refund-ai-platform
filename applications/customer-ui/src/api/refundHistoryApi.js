const refundApiUrl =
    import.meta.env.VITE_REFUND_API_URL
    || "http://localhost:8080";

export async function getRefundHistory(
    taxReturnId,
    accessToken
) {

    const response =
        await fetch(
            `${refundApiUrl}/api/v1/refunds/${taxReturnId}/history`,
            {
                headers:
                    {
                        Authorization:
                            `Bearer ${accessToken}`
                    }
            }
        );

    if (!response.ok) {

        const responseText =
            await response.text();

        const error =
            new Error(
                responseText
                || `History request failed with status ${response.status}`
            );

        error.status =
            response.status;

        throw error;
    }

    return response.json();
}
