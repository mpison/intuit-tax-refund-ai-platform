const refundApiUrl =
    import.meta.env.VITE_REFUND_API_URL || "http://localhost:8080";

export async function createFiledReturn(accessToken, request) {
    const response = await fetch(
        `${refundApiUrl}/api/v1/tax-returns`,
        {
            method: "POST",
            headers: {
                Authorization: `Bearer ${accessToken}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        }
    );

    if (!response.ok) {
        const responseText = await response.text();
        const error = new Error(
            responseText || `Create return failed with status ${response.status}`
        );
        error.status = response.status;
        throw error;
    }

    return response.json();
}
