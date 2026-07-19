import React, {
    useEffect,
    useState
} from "react";

import {
    getLatestRefund,
    getRefundPrediction,
    refreshRefund
} from "./api/refundApi";

import PredictionCard from "./components/PredictionCard";
import PolicyAssistant from "./components/PolicyAssistant";
import CreateFiledReturn from "./components/CreateFiledReturn";
import UserProfile from "./components/UserProfile";
import RefundTimeline from "./components/RefundTimeline";
import RefundHistory from "./components/RefundHistory";
import EmptyRefundState from "./components/EmptyRefundState";
import { getRefundHistory } from "./api/refundHistoryApi";

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
        prediction,
        setPrediction
    ] =
        useState(null);

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

    const [
        hasNoRefund,
        setHasNoRefund
    ] =
        useState(false);

    const [
        isCreatingReturn,
        setIsCreatingReturn
    ] =
        useState(false);

    const [
        activeView,
        setActiveView
    ] =
        useState("refund");

    const [
        refundHistory,
        setRefundHistory
    ] =
        useState([]);

    const [
        isHistoryLoading,
        setIsHistoryLoading
    ] =
        useState(false);

    const [
        historyError,
        setHistoryError
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

            setHasNoRefund(
                false
            );

            setIsCreatingReturn(
                false
            );

            const latestPrediction =
                await getRefundPrediction(
                    latestRefund.taxReturnId,
                    keycloak.token
                );

            setPrediction(
                latestPrediction
            );

            await loadRefundHistory(
                latestRefund.taxReturnId
            );
        }
        catch (error) {

            const errorText =
                String(
                    error?.message
                    || ""
                );

            if (
                error?.status === 404
                || errorText.includes("404")
                || errorText.toLowerCase().includes("not found")
            ) {

                setRefund(
                    null
                );

                setPrediction(
                    null
                );

                setRefundHistory(
                    []
                );

                setHasNoRefund(
                    true
                );

                setErrorMessage(
                    ""
                );
            }
            else {

                setErrorMessage(
                    errorText
                );
            }
        }
        finally {

            setIsLoading(
                false
            );
        }
    }

    async function loadRefundHistory(
        taxReturnId
    ) {

        try {

            setIsHistoryLoading(
                true
            );

            setHistoryError(
                ""
            );

            await keycloak.updateToken(
                30
            );

            const historyResponse =
                await getRefundHistory(
                    taxReturnId,
                    keycloak.token
                );

            setRefundHistory(
                historyResponse.events
                || []
            );
        }
        catch (error) {

            setRefundHistory(
                []
            );

            setHistoryError(
                error.message
            );
        }
        finally {

            setIsHistoryLoading(
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

                <nav className="navActions">

                    <button
                        className={
                            activeView === "refund"
                                ? "primaryNavButton"
                                : "secondaryButton"
                        }
                        onClick={
                            () =>
                                setActiveView(
                                    "refund"
                                )
                        }
                        type="button">

                        Refund

                    </button>

                    <button
                        className={
                            activeView === "profile"
                                ? "primaryNavButton"
                                : "secondaryButton"
                        }
                        onClick={
                            () =>
                                setActiveView(
                                    "profile"
                                )
                        }
                        type="button">

                        Profile

                    </button>

                    <button
                        className="secondaryButton"
                        onClick={
                            () =>
                                keycloak.logout()
                        }
                        type="button">

                        Sign out

                    </button>

                </nav>

            </header>

            <main className="content">

                {
                    activeView === "refund"
                    && errorMessage
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
                    activeView === "refund"
                    && isLoading
                    && (

                        <section className="card">

                            Loading refund...

                        </section>
                    )
                }

                {
                    activeView === "refund"
                    && hasNoRefund
                    && !isLoading
                    && !isCreatingReturn
                    && (

                        <EmptyRefundState
                            onCreate={
                                () =>
                                    setIsCreatingReturn(
                                        true
                                    )
                            }
                        />
                    )
                }

                {
                    activeView === "refund"
                    && hasNoRefund
                    && isCreatingReturn
                    && (

                        <CreateFiledReturn
                            keycloak={keycloak}
                            onCancel={
                                () =>
                                    setIsCreatingReturn(
                                        false
                                    )
                            }
                            onCreated={loadRefund}
                        />
                    )
                }

                {
                    activeView === "refund"
                    && refund
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

                            <PredictionCard
                                prediction={prediction}
                            />

                            <RefundTimeline
                                currentStatus={
                                    refund.status
                                }
                            />

                            <RefundHistory
                                events={
                                    refundHistory
                                }
                                errorMessage={
                                    historyError
                                }
                                isLoading={
                                    isHistoryLoading
                                }
                                onRefresh={
                                    () =>
                                        loadRefundHistory(
                                            refund.taxReturnId
                                        )
                                }
                                statusLabels={
                                    refundStageLabels
                                }
                            />

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

                {
                    activeView === "profile"
                    && (

                        <UserProfile
                            accessToken={
                                keycloak.token
                            }
                        />
                    )
                }

            </main>

            <PolicyAssistant />

        </div>
    );
}