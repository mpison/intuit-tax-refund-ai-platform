import React, { useMemo, useState } from "react";
import { createFiledReturn } from "../api/taxReturnApi";

export default function CreateFiledReturn({
    keycloak,
    onCreated,
    onCancel
}) {
    const defaultFiledDate = useMemo(
        () => new Date().toISOString().slice(0, 10),
        []
    );

    const [form, setForm] = useState({
        taxYear: new Date().getFullYear() - 1,
        filedDate: defaultFiledDate,
        expectedRefundAmount: "",
        filingMethod: "E_FILE",
        externalRefundId: ""
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    function updateField(name, value) {
        setForm(current => ({ ...current, [name]: value }));
    }

    async function submit(event) {
        event.preventDefault();

        try {
            setIsSubmitting(true);
            setErrorMessage("");

            await keycloak.updateToken(30);

            await createFiledReturn(
                keycloak.token,
                {
                    taxYear: Number(form.taxYear),
                    filedAt: `${form.filedDate}T12:00:00Z`,
                    refundAmount: Number(form.expectedRefundAmount),
                    filingMethod: form.filingMethod,
                    externalRefundId: form.externalRefundId.trim() || null
                }
            );

            await onCreated();
        }
        catch (error) {
            setErrorMessage(error.message);
        }
        finally {
            setIsSubmitting(false);
        }
    }

    return (
        <section className="card filedReturnCard">
            <p className="eyebrow">Refund tracking</p>
            <h2>Add filed return</h2>
            <p>Use demo data only.</p>

            <form className="filedReturnForm" onSubmit={submit}>
                <label>
                    Tax year
                    <select
                        value={form.taxYear}
                        onChange={event =>
                            updateField("taxYear", event.target.value)
                        }
                    >
                        <option value="2025">2025</option>
                        <option value="2024">2024</option>
                        <option value="2023">2023</option>
                    </select>
                </label>

                <label>
                    Filed date
                    <input
                        required
                        type="date"
                        value={form.filedDate}
                        onChange={event =>
                            updateField("filedDate", event.target.value)
                        }
                    />
                </label>

                <label>
                    Expected refund amount
                    <input
                        required
                        min="0"
                        step="0.01"
                        type="number"
                        value={form.expectedRefundAmount}
                        onChange={event =>
                            updateField(
                                "expectedRefundAmount",
                                event.target.value
                            )
                        }
                    />
                </label>

                <label>
                    Filing method
                    <select
                        value={form.filingMethod}
                        onChange={event =>
                            updateField("filingMethod", event.target.value)
                        }
                    >
                        <option value="E_FILE">E-file</option>
                        <option value="PAPER">Paper</option>
                    </select>
                </label>

                <label className="fullWidthField">
                    IRS reference number
                    <input
                        placeholder="Auto-generated when blank"
                        value={form.externalRefundId}
                        onChange={event =>
                            updateField("externalRefundId", event.target.value)
                        }
                    />
                </label>

                <div className="filedReturnActions">
                    <button
                        className="secondaryButton"
                        disabled={isSubmitting}
                        onClick={onCancel}
                        type="button"
                    >
                        Cancel
                    </button>

                    <button disabled={isSubmitting} type="submit">
                        {isSubmitting ? "Creating..." : "Create return"}
                    </button>
                </div>
            </form>

            {errorMessage && (
                <p className="errorMessage">{errorMessage}</p>
            )}
        </section>
    );
}
