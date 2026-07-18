import React, {
    useEffect,
    useMemo,
    useState
} from "react";

import {
    getLatestRefund
} from "./api/refundApi";

const refundStages =
    [
        "FILED",
        "ACCEPTED",
        "PROCESSING",
        "APPROVED",
        "REFUND_SENT",
        "REFUND_RECEIVED"
    ];

const refundStageLabels =
    {
        FILED:
            "Filed",

        ACCEPTED:
            "Accepted",

        PROCESSING:
            "Processing",

        APPROVED:
            "Approved",

        REFUND_SENT:
            "Refund sent",

        REFUND_RECEIVED:
            "Refund received"
    };

export default function App(
    {
        keycloak
    }
) {

    const [
        refund,
        setRefund
    ] =
        useState(null);

    const [
        errorMessage,
        setErrorMessage
    ] =
        useState("");

    const [
        isLoading,
        setIsLoading
    ] =
        useState(true);

    async function loadRefund() {

        try {

            setIsLoading(
                true
            );

            setErrorMessage(
                ""
            );

            await keycloak.updateToken(
                30
            );

            const latestRefund =
                await getLatestRefund(
                    keycloak.token
                );

            setRefund(
                latestRefund
            );
        }
        catch (error) {

            setErrorMessage(
                error.message
            );
        }
        finally {

            setIsLoading(
                false
            );
        }
    }

    useEffect(
        () => {

            loadRefund();
        },
        []
    );

    const currentStageIndex =
        useMemo(
            () => {

                if (!refund) {

                    return -1;
                }

                return refundStages.indexOf(
                    refund.status
                );
            },
            [
                refund
            ]
        );

    return (

        <div className="page">

            <header className="topbar">

                <div>

                    <div className="brand">
                        Refund Platform
                    </div>

                    <div className="subtitle">
                        Secure refund tracking
                    </div>

                </div>

                <div className="profile">

                    <span>
                        {
                            keycloak
                                .tokenParsed
                                ?.preferred_username
                        }
                    </span>

                    <button
                        className="secondaryButton"
                        onClick={
                            () =>
                                keycloak.logout()
                        }>

                        Sign out

                    </button>

                </div>

            </header>

            <main className="content">

                {
                    isLoading
                    && (

                        <section className="card">

                            Loading refund...

                        </section>
                    )
                }

                {
                    errorMessage
                    && (

                        <section className="card errorCard">

                            <h2>
                                Unable to load refund
                            </h2>

                            <p>
                                {errorMessage}
                            </p>

                            <button
                                onClick={loadRefund}>

                                Try again

                            </button>

                        </section>
                    )
                }

                {
                    refund
                    && !isLoading
                    && (

                        <>

                            <section className="hero">

                                <div>

                                    <p className="eyebrow">

                                        Your {refund.taxYear}
                                        {" "}
                                        federal refund

                                    </p>

                                    <h1>
                                        {
                                            formatCurrency(
                                                refund.refundAmount
                                            )
                                        }
                                    </h1>

                                    <p>

                                        Current status:
                                        {" "}

                                        <strong>

                                            {
                                                refundStageLabels[
                                                    refund.status
                                                ]
                                                || refund.status
                                            }

                                        </strong>

                                    </p>

                                </div>

                                <button
                                    onClick={loadRefund}>

                                    Refresh status

                                </button>

                            </section>

                            <section className="card">

                                <h2>
                                    Refund progress
                                </h2>

                                <div className="refundTimeline">

                                    {
                                        refundStages.map(
                                            (
                                                refundStage,
                                                refundStageIndex
                                            ) => {

                                                const isCompleted =
                                                    refundStageIndex
                                                    < currentStageIndex;

                                                const isCurrent =
                                                    refundStageIndex
                                                    === currentStageIndex;

                                                const isActive =
                                                    refundStageIndex
                                                    <= currentStageIndex;

                                                return (

                                                    <div
                                                        className="refundStage"
                                                        key={refundStage}>

                                                        <div
                                                            className={
                                                                isActive
                                                                    ? "refundStageMarker active"
                                                                    : "refundStageMarker"
                                                            }>

                                                            {
                                                                isCompleted
                                                                    ? "✓"
                                                                    : isCurrent
                                                                        ? "●"
                                                                        : ""
                                                            }

                                                        </div>

                                                        <div
                                                            className={
                                                                isCurrent
                                                                    ? "refundStageLabel current"
                                                                    : "refundStageLabel"
                                                            }>

                                                            {
                                                                refundStageLabels[
                                                                    refundStage
                                                                ]
                                                            }

                                                        </div>

                                                    </div>
                                                );
                                            }
                                        )
                                    }

                                </div>

                            </section>

                            <section className="detailsGrid">

                                <section className="card">

                                    <h2>
                                        What happens next?
                                    </h2>

                                    <p>
                                        {refund.guidance}
                                    </p>

                                </section>

                                <section className="card">

                                    <h2>
                                        Refund details
                                    </h2>

                                    <dl>

                                        <div>

                                            <dt>
                                                Customer
                                            </dt>

                                            <dd>
                                                {refund.customerName}
                                            </dd>

                                        </div>

                                        <div>

                                            <dt>
                                                Filed
                                            </dt>

                                            <dd>
                                                {
                                                    formatDate(
                                                        refund.filedAt
                                                    )
                                                }
                                            </dd>

                                        </div>

                                        <div>

                                            <dt>
                                                Last checked
                                            </dt>

                                            <dd>
                                                {
                                                    formatDateTime(
                                                        refund.lastCheckedAt
                                                    )
                                                }
                                            </dd>

                                        </div>

                                    </dl>

                                </section>

                            </section>

                        </>
                    )
                }

            </main>

        </div>
    );
}

function formatCurrency(
    value
) {

    return new Intl.NumberFormat(
        "en-US",
        {
            style:
                "currency",

            currency:
                "USD"
        }
    )
    .format(
        value
    );
}

function formatDate(
    value
) {

    return new Intl.DateTimeFormat(
        "en-US",
        {
            dateStyle:
                "long"
        }
    )
    .format(
        new Date(value)
    );
}

function formatDateTime(
    value
) {

    return new Intl.DateTimeFormat(
        "en-US",
        {
            dateStyle:
                "medium",

            timeStyle:
                "short"
        }
    )
    .format(
        new Date(value)
    );
}
