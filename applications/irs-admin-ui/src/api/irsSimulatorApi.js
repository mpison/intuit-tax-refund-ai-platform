const apiUrl =
    import.meta.env.VITE_IRS_SIMULATOR_API_URL
    || "http://localhost:8090";

async function parse(
    response
) {

    if (
        response.status === 204
    ) {

        return null;
    }

    const contentType =
        response.headers.get(
            "content-type"
        )
        || "";

    const body =
        contentType.includes(
            "application/json"
        )
            ? await response.json()
            : await response.text();

    if (
        !response.ok
    ) {

        const message =
            typeof body === "string"
                ? body
                : body?.message
                    || body?.detail
                    || body?.error;

        throw new Error(
            message
            || `Request failed with status ${response.status}`
        );
    }

    return body;
}

export async function listRefunds() {

    return parse(
        await fetch(
            `${apiUrl}/api/v1/demo/irs/refunds`
        )
    );
}

export async function createRefund(
    request
) {

    return parse(
        await fetch(
            `${apiUrl}/api/v1/demo/irs/refunds`,
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
                        request
                    )
            }
        )
    );
}

export async function updateRefund(
    externalRefundId,
    request
) {

    return parse(
        await fetch(
            `${apiUrl}/api/v1/demo/irs/refunds/${encodeURIComponent(externalRefundId)}/status`,
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
                        request
                    )
            }
        )
    );
}

export async function deleteRefund(
    externalRefundId
) {

    return parse(
        await fetch(
            `${apiUrl}/api/v1/demo/irs/refunds/${encodeURIComponent(externalRefundId)}`,
            {
                method:
                    "DELETE"
            }
        )
    );
}
