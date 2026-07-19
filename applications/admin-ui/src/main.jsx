import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import keycloak from "./keycloak";
import "./styles.css";

async function start() {
  const authenticated = await keycloak.init({
    onLoad: "login-required",
    pkceMethod: "S256",
    checkLoginIframe: false
  });

  if (!authenticated) {
    await keycloak.login();
    return;
  }

  createRoot(document.getElementById("root"))
    .render(
      <React.StrictMode>
        <App keycloak={keycloak} />
      </React.StrictMode>
    );
}

start();
