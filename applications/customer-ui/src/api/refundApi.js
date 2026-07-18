const refundApiUrl =
    import.meta.env.VITE_REFUND_API_URL
    || "http://localhost:8080";

export async function getLatestRefund(
    accessToken
) {

    const response =
        await fetch(
            `${refundApiUrl}/api/v1/refunds/latest`,
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

export async function refreshRefund(
    taxReturnId,
    accessToken
) {

    const response =
        await fetch(
            `${refundApiUrl}/api/v1/refunds/${taxReturnId}/refresh`,
            {
                method:
                    "POST",

                headers:
                    {
                        Authorization:
                            `Bearer ${accessToken}`
                    }
            }
        );

    if (!response.ok) {

        throw new Error(
            `Refund refresh failed with status ${response.status}`
        );
    }

    return response.json();
}


export async function getRefundPrediction(
    taxReturnId,
    accessToken
) {
    const response = await fetch(
        `${refundApiUrl}/api/v1/refunds/${taxReturnId}/prediction`,
        {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }
    );

    if (!response.ok) {
        throw new Error(
            `Prediction API failed with status ${response.status}`
        );
    }

    return response.json();
}
