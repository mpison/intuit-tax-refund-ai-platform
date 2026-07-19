import React, {
    useEffect,
    useState
} from "react";

import {
    getCurrentUserProfile,
    updateCurrentUserProfile
} from "../api/userProfileApi";

export default function UserProfile({
    accessToken
}) {
    const [profile, setProfile] = useState(null);
    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        displayName: "",
        phoneNumber: ""
    });
    const [isSaving, setIsSaving] = useState(false);
    const [statusMessage, setStatusMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        loadProfile();
    }, [accessToken]);

    async function loadProfile() {
        try {
            setErrorMessage("");

            const currentProfile =
                await getCurrentUserProfile(accessToken);

            setProfile(currentProfile);
            setForm({
                firstName: currentProfile.firstName || "",
                lastName: currentProfile.lastName || "",
                displayName: currentProfile.displayName || "",
                phoneNumber: currentProfile.phoneNumber || ""
            });
        }
        catch (error) {
            setErrorMessage(error.message);
        }
    }

    async function saveProfile(event) {
        event.preventDefault();

        try {
            setIsSaving(true);
            setStatusMessage("");
            setErrorMessage("");

            const updatedProfile =
                await updateCurrentUserProfile(
                    accessToken,
                    form
                );

            setProfile(updatedProfile);
            setStatusMessage("Profile saved.");
        }
        catch (error) {
            setErrorMessage(error.message);
        }
        finally {
            setIsSaving(false);
        }
    }

    if (!profile) {
        return (
            <section className="card profileCard">
                {errorMessage || "Loading profile..."}
            </section>
        );
    }

    return (
        <section className="card profileCard">
            <div className="profileHeader">
                <div>
                    <p className="eyebrow">Account</p>
                    <h2>Your profile</h2>
                    <p>Keep your contact information current.</p>
                </div>

                <div className="profileIdentity">
                    <strong>{profile.email}</strong>
                    <span>Keycloak account</span>
                </div>
            </div>

            <form
                className="profileForm"
                onSubmit={saveProfile}
            >
                <label>
                    First name
                    <input
                        required
                        value={form.firstName}
                        onChange={event =>
                            setForm(current => ({
                                ...current,
                                firstName: event.target.value
                            }))
                        }
                    />
                </label>

                <label>
                    Last name
                    <input
                        required
                        value={form.lastName}
                        onChange={event =>
                            setForm(current => ({
                                ...current,
                                lastName: event.target.value
                            }))
                        }
                    />
                </label>

                <label>
                    Display name
                    <input
                        required
                        value={form.displayName}
                        onChange={event =>
                            setForm(current => ({
                                ...current,
                                displayName: event.target.value
                            }))
                        }
                    />
                </label>

                <label>
                    Phone number
                    <input
                        value={form.phoneNumber}
                        onChange={event =>
                            setForm(current => ({
                                ...current,
                                phoneNumber: event.target.value
                            }))
                        }
                    />
                </label>

                <div className="profileActions">
                    <button
                        disabled={isSaving}
                        type="submit"
                    >
                        {isSaving ? "Saving..." : "Save profile"}
                    </button>
                </div>
            </form>

            {statusMessage && (
                <p className="successMessage">
                    {statusMessage}
                </p>
            )}

            {errorMessage && (
                <p className="errorMessage">
                    {errorMessage}
                </p>
            )}
        </section>
    );
}
