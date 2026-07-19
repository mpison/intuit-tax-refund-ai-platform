import React from "react";

export default function RefundHistory({
    events,
    errorMessage,
    isLoading,
    onRefresh,
    statusLabels
}) {
    return (
        <section className="card historyCard">
            <div className="historyHeader">
                <div>
                    <p className="eyebrow">
                        Status history
                    </p>

                    <h2>
                        Recent activity
                    </h2>
                </div>

                <button
                    className="secondaryButton"
                    disabled={isLoading}
                    onClick={onRefresh}
                    type="button"
                >
                    {
                        isLoading
                            ? "Loading..."
                            : "Refresh activity"
                    }
                </button>
            </div>

            {
                errorMessage
                && (
                    <p className="historyError">
                        {errorMessage}
                    </p>
                )
            }

            {
                !isLoading
                && !errorMessage
                && events.length === 0
                && (
                    <p className="historyEmpty">
                        No status activity is available yet.
                    </p>
                )
            }

            {
                events.length > 0
                && (
                    <div className="historyList">
                        {
                            events.map(
                                event => {
                                    const previousLabel =
                                        event.previousStatus
                                            ? (
                                                statusLabels[
                                                    event.previousStatus
                                                ]
                                                || event.previousStatus
                                            )
                                            : null;

                                    const currentLabel =
                                        statusLabels[
                                            event.newStatus
                                        ]
                                        || event.newStatus;

                                    return (
                                        <article
                                            className="historyItem"
                                            key={
                                                event.historyId
                                            }
                                        >
                                            <div className="historyMarker">
                                                ●
                                            </div>

                                            <div>
                                                <strong>
                                                    {
                                                        currentLabel
                                                    }
                                                </strong>

                                                <p>
                                                    {
                                                        previousLabel
                                                            ? `${previousLabel} → ${currentLabel}`
                                                            : "Initial refund status"
                                                    }
                                                </p>

                                                <span>
                                                    {
                                                        event.source
                                                        || "Refund Platform"
                                                    }
                                                    {" · "}
                                                    {
                                                        event.changedAt
                                                            ? new Date(
                                                                event.changedAt
                                                            )
                                                            .toLocaleString()
                                                            : "Time unavailable"
                                                    }
                                                </span>
                                            </div>
                                        </article>
                                    );
                                }
                            )
                        }
                    </div>
                )
            }
        </section>
    );
}
