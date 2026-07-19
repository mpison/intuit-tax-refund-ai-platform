import Keycloak from "keycloak-js";

export default new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8081",
  realm: import.meta.env.VITE_KEYCLOAK_REALM || "refund-platform",
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || "admin-ui"
});
