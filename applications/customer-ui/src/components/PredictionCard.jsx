import React from "react";

function formatDate(
    value
) {
    if (!value) {
        return "Not available";
    }

    const dateValue =
        new Date(
            value
        );

    if (
        Number.isNaN(
            dateValue.getTime()
        )
    ) {
        return String(
            value
        );
    }

    return dateValue
        .toLocaleDateString(
            "en-US",
            {
                month: "short",
                day: "numeric",
                year: "numeric"
            }
        );
}

function normalizeConfidence(
    prediction
) {
    const rawConfidence =
        prediction?.confidence
        ?? prediction?.confidenceScore
        ?? prediction?.probability;

    if (
        rawConfidence === null
        || rawConfidence === undefined
        || rawConfidence === ""
    ) {
        return null;
    }

    const confidenceNumber =
        Number(
            rawConfidence
        );

    if (
        Number.isNaN(
            confidenceNumber
        )
    ) {
        return null;
    }

    const percentage =
        confidenceNumber <= 1
            ? confidenceNumber * 100
            : confidenceNumber;

    return Math.max(
        0,
        Math.min(
            100,
            Math.round(
                percentage
            )
        )
    );
}

export default function PredictionCard({
    prediction
}) {
    if (!prediction) {
        return (
            <section className="card predictionCard predictionCardEmpty">
                <div>
                    <p className="predictionEyebrow">
                        AI refund prediction
                    </p>

                    <h2>
                        Prediction unavailable
                    </h2>

                    <p>
                        A prediction will appear after enough refund
                        information is available.
                    </p>
                </div>
            </section>
        );
    }

    const estimatedDate =
        prediction.estimatedDepositDate
        ?? prediction.estimatedRefundDate
        ?? prediction.predictedDate
        ?? prediction.predictedDepositDate;

    const daysRemaining =
        prediction.daysRemaining
        ?? prediction.estimatedDaysRemaining
        ?? prediction.predictedDays;

    const confidence =
        normalizeConfidence(
            prediction
        );

    const modelName =
        prediction.modelName
        ?? prediction.modelVersion
        ?? prediction.model
        ?? "Refund ETA model";

    const explanation =
        prediction.explanation
        ?? prediction.reason
        ?? prediction.summary
        ?? "The estimate uses filing date, refund status, and processing patterns.";

    return (
        <section className="card predictionCard enhancedPredictionCard">
            <div className="predictionMain">
                <p className="predictionEyebrow">
                    AI refund prediction
                </p>

                <h2>
                    {
                        formatDate(
                            estimatedDate
                        )
                    }
                </h2>

                <p className="predictionExplanation">
                    {explanation}
                </p>

                <div className="predictionModel">
                    <span>
                        Model
                    </span>

                    <strong>
                        {modelName}
                    </strong>
                </div>
            </div>

            <div className="predictionMetrics">
                <div>
                    <span>
                        Days remaining
                    </span>

                    <strong>
                        {
                            daysRemaining
                            ?? "—"
                        }
                    </strong>
                </div>

                <div>
                    <span>
                        Confidence
                    </span>

                    <strong>
                        {
                            confidence === null
                                ? "—"
                                : `${confidence}%`
                        }
                    </strong>
                </div>
            </div>
        </section>
    );
}
