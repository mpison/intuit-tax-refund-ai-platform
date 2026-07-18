import React from "react";

export default function PredictionCard({ prediction }) {
    if (!prediction) {
        return <section className="card">Loading prediction...</section>;
    }

    return (
        <section className="card predictionCard">
            <p className="predictionEyebrow">Estimated refund arrival</p>
            <h2>{prediction.predictedRefundDate}</h2>

            <div className="predictionMetrics">
                <div>
                    <span>Days remaining</span>
                    <strong>{prediction.estimatedDaysRemaining}</strong>
                </div>

                <div>
                    <span>Confidence</span>
                    <strong>{Math.round(prediction.confidenceScore * 100)}%</strong>
                </div>
            </div>

            <p>{prediction.explanation}</p>
        </section>
    );
}
