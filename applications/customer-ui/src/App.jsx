import React, {
    useEffect,
    useState
} from "react";

import {
    getLatestRefund,
    refreshRefund
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
        isLoading,
        setIsLoading
    ] =
        useState(true);

    const [
        isRefreshing,
        setIsRefreshing
    ] =
        useState(false);

    const [
        errorMessage,
        setErrorMessage
    ] =
        useState("");

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

    async function refreshFromIrs() {

        try {

            setIsRefreshing(
                true
            );

            setErrorMessage(
                ""
            );

            await keycloak.updateToken(
                30
            );

            await refreshRefund(
                refund.taxReturnId,
                keycloak.token
            );

            await loadRefund();
        }
        catch (error) {

            setErrorMessage(
                error.message
            );
        }
        finally {

            setIsRefreshing(
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
        refund
            ? refundStages.indexOf(
                    refund.status
              )
            : -1;

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

                <button
                    className="secondaryButton"
                    onClick={
                        () =>
                            keycloak.logout()
                    }>

                    Sign out

                </button>

            </header>

            <main className="content">

                {
                    errorMessage
                    && (

                        <section className="card errorCard">

                            <h2>
                                Unable to complete request
                            </h2>

                            <p>
                                {errorMessage}
                            </p>

                        </section>
                    )
                }

                {
                    isLoading
                    && (

                        <section className="card">

                            Loading refund...

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
                                        Your {refund.taxYear} federal refund
                                    </p>

                                    <h1>
                                        {
                                            new Intl.NumberFormat(
                                                "en-US",
                                                {
                                                    style:
                                                        "currency",

                                                    currency:
                                                        "USD"
                                                }
                                            )
                                            .format(
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
                                    disabled={isRefreshing}
                                    onClick={refreshFromIrs}>

                                    {
                                        isRefreshing
                                            ? "Refreshing..."
                                            : "Refresh from IRS"
                                    }

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
                                                stage,
                                                stageIndex
                                            ) => (

                                                <div
                                                    className="refundStage"
                                                    key={stage}>

                                                    <div
                                                        className={
                                                            stageIndex
                                                                <= currentStageIndex
                                                                ? "refundStageMarker active"
                                                                : "refundStageMarker"
                                                        }>

                                                        {
                                                            stageIndex
                                                                < currentStageIndex
                                                                ? "✓"
                                                                : stageIndex
                                                                    === currentStageIndex
                                                                    ? "●"
                                                                    : ""
                                                        }

                                                    </div>

                                                    <div
                                                        className={
                                                            stageIndex
                                                                === currentStageIndex
                                                                ? "refundStageLabel current"
                                                                : "refundStageLabel"
                                                        }>

                                                        {
                                                            refundStageLabels[
                                                                stage
                                                            ]
                                                        }

                                                    </div>

                                                </div>
                                            )
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
                                                    new Date(
                                                        refund.filedAt
                                                    )
                                                    .toLocaleDateString()
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
