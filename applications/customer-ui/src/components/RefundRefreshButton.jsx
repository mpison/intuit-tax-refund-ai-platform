import React, {
    useState
} from "react";

import {
    refreshRefund
} from "../api/refundApi";

export default function RefundRefreshButton(
    {
        taxReturnId,
        accessToken,
        onRefreshed
    }
) {

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

    async function handleRefresh() {

        try {

            setIsRefreshing(
                true
            );

            setErrorMessage(
                ""
            );

            await refreshRefund(
                taxReturnId,
                accessToken
            );

            await onRefreshed();
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

    return (

        <div>

            <button
                disabled={isRefreshing}
                onClick={handleRefresh}>

                {
                    isRefreshing
                        ? "Refreshing..."
                        : "Refresh from IRS"
                }

            </button>

            {
                errorMessage
                && (

                    <p className="errorText">
                        {errorMessage}
                    </p>
                )
            }

        </div>
    );
}
