import React from "react";

import { createRoot } from "react-dom/client";

import App from "./App";

import { keycloak } from "./auth/keycloak";

import "./styles.css";
import "./RefundFaq.css";

keycloak
    .init(
        {
            onLoad:
                "login-required",

            pkceMethod:
                "S256",

            checkLoginIframe:
                false
        }
    )
    .then(
        () => {

            createRoot(
                document.getElementById("root")
            )
            .render(
                <React.StrictMode>

                    <App
                        keycloak={keycloak}
                    />

                </React.StrictMode>
            );
        }
    );
