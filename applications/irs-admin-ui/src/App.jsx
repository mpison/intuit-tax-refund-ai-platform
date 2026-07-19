import React, {
    useEffect,
    useMemo,
    useState
} from "react";

import {
    createRefund,
    deleteRefund,
    listRefunds,
    updateRefund
} from "./api/irsSimulatorApi";

const statuses = [
    "FILED",
    "ACCEPTED",
    "PROCESSING",
    "ADDITIONAL_REVIEW",
    "ACTION_REQUIRED",
    "APPROVED",
    "REFUND_SENT",
    "REFUND_RECEIVED"
];

export default function App() {

    const [
        refunds,
        setRefunds
    ] =
        useState([]);

    const [
        selectedRefund,
        setSelectedRefund
    ] =
        useState(null);

    const [
        searchText,
        setSearchText
    ] =
        useState("");

    const [
        createForm,
        setCreateForm
    ] =
        useState(
            {
                externalRefundId:
                    "",

                status:
                    "FILED",

                officialRefundDate:
                    ""
            }
        );

    const [
        editForm,
        setEditForm
    ] =
        useState(
            {
                status:
                    "PROCESSING",

                officialRefundDate:
                    ""
            }
        );

    const [
        isLoading,
        setIsLoading
    ] =
        useState(true);

    const [
        isSaving,
        setIsSaving
    ] =
        useState(false);

    const [
        message,
        setMessage
    ] =
        useState("");

    const [
        errorMessage,
        setErrorMessage
    ] =
        useState("");

    const filteredRefunds =
        useMemo(
            () => {

                const normalizedSearch =
                    searchText
                        .trim()
                        .toLowerCase();

                if (
                    !normalizedSearch
                ) {

                    return refunds;
                }

                return refunds.filter(
                    refund =>
                        refund.externalRefundId
                            .toLowerCase()
                            .includes(
                                normalizedSearch
                            )
                        || refund.status
                            .toLowerCase()
                            .includes(
                                normalizedSearch
                            )
                );
            },
            [
                refunds,
                searchText
            ]
        );

    async function loadRefunds() {

        try {

            setIsLoading(
                true
            );

            setErrorMessage(
                ""
            );

            setRefunds(
                await listRefunds()
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

            loadRefunds();
        },
        []
    );

    function selectRefund(
        refund
    ) {

        setSelectedRefund(
            refund
        );

        setEditForm(
            {
                status:
                    refund.status,

                officialRefundDate:
                    refund.officialRefundDate
                    || ""
            }
        );

        setMessage(
            ""
        );

        setErrorMessage(
            ""
        );
    }

    async function submitCreate(
        event
    ) {

        event.preventDefault();

        try {

            setIsSaving(
                true
            );

            setMessage(
                ""
            );

            setErrorMessage(
                ""
            );

            await createRefund(
                {
                    externalRefundId:
                        createForm.externalRefundId.trim(),

                    status:
                        createForm.status,

                    officialRefundDate:
                        createForm.officialRefundDate
                        || null
                }
            );

            setCreateForm(
                {
                    externalRefundId:
                        "",

                    status:
                        "FILED",

                    officialRefundDate:
                        ""
                }
            );

            setMessage(
                "Demo IRS refund created."
            );

            await loadRefunds();
        }
        catch (error) {

            setErrorMessage(
                error.message
            );
        }
        finally {

            setIsSaving(
                false
            );
        }
    }

    async function submitUpdate(
        event
    ) {

        event.preventDefault();

        try {

            setIsSaving(
                true
            );

            setMessage(
                ""
            );

            setErrorMessage(
                ""
            );

            const updatedRefund =
                await updateRefund(
                    selectedRefund.externalRefundId,
                    {
                        status:
                            editForm.status,

                        officialRefundDate:
                            editForm.officialRefundDate
                            || null
                    }
                );

            setSelectedRefund(
                updatedRefund
            );

            setMessage(
                "IRS simulator status updated."
            );

            await loadRefunds();
        }
        catch (error) {

            setErrorMessage(
                error.message
            );
        }
        finally {

            setIsSaving(
                false
            );
        }
    }

    async function removeRefund(
        refund
    ) {

        if (
            !window.confirm(
                `Delete ${refund.externalRefundId}?`
            )
        ) {

            return;
        }

        try {

            setErrorMessage(
                ""
            );

            await deleteRefund(
                refund.externalRefundId
            );

            if (
                selectedRefund?.externalRefundId
                === refund.externalRefundId
            ) {

                setSelectedRefund(
                    null
                );
            }

            setMessage(
                "Demo IRS refund deleted."
            );

            await loadRefunds();
        }
        catch (error) {

            setErrorMessage(
                error.message
            );
        }
    }

    return (
        <div className="page">
            <header className="topbar">
                <div>
                    <div className="brand">
                        IRS Simulator Console
                    </div>

                    <div className="subtitle">
                        Local demo mechanism · PostgreSQL-backed
                    </div>
                </div>

                <button
                    className="secondaryButton"
                    onClick={loadRefunds}
                    type="button"
                >
                    Refresh
                </button>
            </header>

            <main className="content">
                <section className="hero">
                    <div>
                        <p className="eyebrow">
                            Demo operations
                        </p>

                        <h1>
                            Simulate IRS refund updates
                        </h1>

                        <p>
                            Modify external IRS records, then refresh the
                            customer dashboard to synchronize the result.
                        </p>
                    </div>

                    <span className="demoBadge">
                        LOCAL ONLY
                    </span>
                </section>

                {
                    errorMessage
                    && (
                        <section className="card errorCard">
                            <strong>
                                Unable to complete request
                            </strong>

                            <p>
                                {errorMessage}
                            </p>
                        </section>
                    )
                }

                {
                    message
                    && (
                        <section className="card successCard">
                            {message}
                        </section>
                    )
                }

                <section className="dashboardGrid">
                    <section className="card">
                        <h2>
                            Create demo refund
                        </h2>

                        <form
                            className="formGrid"
                            onSubmit={submitCreate}
                        >
                            <label className="fullWidth">
                                IRS reference number

                                <input
                                    required
                                    placeholder="IRS-DEMO-2025-0002"
                                    value={
                                        createForm.externalRefundId
                                    }
                                    onChange={
                                        event =>
                                            setCreateForm(
                                                current =>
                                                    ({
                                                        ...current,
                                                        externalRefundId:
                                                            event.target.value
                                                    })
                                            )
                                    }
                                />
                            </label>

                            <label>
                                Initial status

                                <select
                                    value={createForm.status}
                                    onChange={
                                        event =>
                                            setCreateForm(
                                                current =>
                                                    ({
                                                        ...current,
                                                        status:
                                                            event.target.value
                                                    })
                                            )
                                    }
                                >
                                    {
                                        statuses.map(
                                            status => (
                                                <option
                                                    key={status}
                                                    value={status}
                                                >
                                                    {status}
                                                </option>
                                            )
                                        )
                                    }
                                </select>
                            </label>

                            <label>
                                Official refund date

                                <input
                                    type="date"
                                    value={
                                        createForm.officialRefundDate
                                    }
                                    onChange={
                                        event =>
                                            setCreateForm(
                                                current =>
                                                    ({
                                                        ...current,
                                                        officialRefundDate:
                                                            event.target.value
                                                    })
                                            )
                                    }
                                />
                            </label>

                            <button
                                className="fullWidth"
                                disabled={isSaving}
                                type="submit"
                            >
                                Create refund
                            </button>
                        </form>
                    </section>

                    <section className="card">
                        <div className="sectionHeader">
                            <div>
                                <h2>
                                    IRS refund records
                                </h2>

                                <p>
                                    {refunds.length} records
                                </p>
                            </div>

                            <input
                                className="searchInput"
                                placeholder="Search"
                                value={searchText}
                                onChange={
                                    event =>
                                        setSearchText(
                                            event.target.value
                                        )
                                }
                            />
                        </div>

                        {
                            isLoading
                                ? (
                                    <p>
                                        Loading records...
                                    </p>
                                )
                                : (
                                    <div className="refundTable">
                                        {
                                            filteredRefunds.map(
                                                refund => (
                                                    <article
                                                        className={
                                                            selectedRefund
                                                                ?.externalRefundId
                                                                === refund.externalRefundId
                                                                ? "refundRow selected"
                                                                : "refundRow"
                                                        }
                                                        key={
                                                            refund.externalRefundId
                                                        }
                                                    >
                                                        <button
                                                            className="rowMain"
                                                            onClick={
                                                                () =>
                                                                    selectRefund(
                                                                        refund
                                                                    )
                                                            }
                                                            type="button"
                                                        >
                                                            <strong>
                                                                {
                                                                    refund.externalRefundId
                                                                }
                                                            </strong>

                                                            <span
                                                                className={
                                                                    `status status-${refund.status}`
                                                                }
                                                            >
                                                                {refund.status}
                                                            </span>

                                                            <small>
                                                                {
                                                                    refund.updatedAt
                                                                        ? new Date(
                                                                            refund.updatedAt
                                                                        )
                                                                        .toLocaleString()
                                                                        : "Never updated"
                                                                }
                                                            </small>
                                                        </button>

                                                        <button
                                                            className="deleteButton"
                                                            onClick={
                                                                () =>
                                                                    removeRefund(
                                                                        refund
                                                                    )
                                                            }
                                                            type="button"
                                                        >
                                                            Delete
                                                        </button>
                                                    </article>
                                                )
                                            )
                                        }

                                        {
                                            filteredRefunds.length === 0
                                            && (
                                                <p>
                                                    No matching records.
                                                </p>
                                            )
                                        }
                                    </div>
                                )
                        }
                    </section>
                </section>

                {
                    selectedRefund
                    && (
                        <section className="card editorCard">
                            <div className="sectionHeader">
                                <div>
                                    <p className="eyebrow">
                                        Selected record
                                    </p>

                                    <h2>
                                        {
                                            selectedRefund.externalRefundId
                                        }
                                    </h2>
                                </div>

                                <span
                                    className={
                                        `status status-${selectedRefund.status}`
                                    }
                                >
                                    {selectedRefund.status}
                                </span>
                            </div>

                            <form
                                className="formGrid"
                                onSubmit={submitUpdate}
                            >
                                <label>
                                    New status

                                    <select
                                        value={editForm.status}
                                        onChange={
                                            event =>
                                                setEditForm(
                                                    current =>
                                                        ({
                                                            ...current,
                                                            status:
                                                                event.target.value
                                                        })
                                                )
                                        }
                                    >
                                        {
                                            statuses.map(
                                                status => (
                                                    <option
                                                        key={status}
                                                        value={status}
                                                    >
                                                        {status}
                                                    </option>
                                                )
                                            )
                                        }
                                    </select>
                                </label>

                                <label>
                                    Official refund date

                                    <input
                                        type="date"
                                        value={
                                            editForm.officialRefundDate
                                        }
                                        onChange={
                                            event =>
                                                setEditForm(
                                                    current =>
                                                        ({
                                                            ...current,
                                                            officialRefundDate:
                                                                event.target.value
                                                        })
                                                )
                                        }
                                    />
                                </label>

                                <button
                                    className="fullWidth"
                                    disabled={isSaving}
                                    type="submit"
                                >
                                    {
                                        isSaving
                                            ? "Saving..."
                                            : "Update simulated status"
                                    }
                                </button>
                            </form>
                        </section>
                    )
                }
            </main>
        </div>
    );
}
