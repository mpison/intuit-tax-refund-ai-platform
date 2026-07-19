import React from "react";

const refundStages = [
    "FILED",
    "ACCEPTED",
    "PROCESSING",
    "APPROVED",
    "REFUND_SENT",
    "REFUND_RECEIVED"
];

const refundStageLabels = {
    FILED: "Filed",
    ACCEPTED: "Accepted",
    PROCESSING: "Processing",
    APPROVED: "Approved",
    REFUND_SENT: "Refund sent",
    REFUND_RECEIVED: "Refund received"
};

export default function RefundTimeline({
    currentStatus
}) {
    const currentStageIndex =
        refundStages.indexOf(
            currentStatus
        );

    return (
        <section className="card">
            <div className="timelineHeader">
                <div>
                    <p className="eyebrow">
                        Current journey
                    </p>

                    <h2>
                        Refund progress
                    </h2>
                </div>

                <span className="statusPill">
                    {
                        refundStageLabels[
                            currentStatus
                        ]
                        || currentStatus
                    }
                </span>
            </div>

            <div className="refundTimeline">
                {
                    refundStages.map(
                        (
                            stage,
                            stageIndex
                        ) => {
                            const isComplete =
                                stageIndex
                                < currentStageIndex;

                            const isCurrent =
                                stageIndex
                                === currentStageIndex;

                            const markerClass =
                                stageIndex
                                    <= currentStageIndex
                                    ? "refundStageMarker active"
                                    : "refundStageMarker";

                            return (
                                <div
                                    className="refundStage"
                                    key={stage}
                                >
                                    <div
                                        className={
                                            markerClass
                                        }
                                    >
                                        {
                                            isComplete
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
                                        }
                                    >
                                        {
                                            refundStageLabels[
                                                stage
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
    );
}
