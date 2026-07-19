import React from "react";

export default function EmptyRefundState({
    onCreate
}) {
    return (
        <section className="card emptyStateCard">
            <div className="emptyStateIcon">
                ↗
            </div>

            <p className="eyebrow">
                Welcome to Refund Platform
            </p>

            <h2>
                No filed return found
            </h2>

            <p className="emptyStateDescription">
                Add a demo filed return to begin tracking status,
                predictions, IRS updates, and refund history.
            </p>

            <button
                onClick={onCreate}
                type="button"
            >
                Add filed return
            </button>

            <p className="emptyStateFootnote">
                Use demo data only. Do not enter real tax identifiers.
            </p>
        </section>
    );
}
