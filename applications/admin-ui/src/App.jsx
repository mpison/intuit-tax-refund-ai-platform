import React, { useEffect, useState } from "react";
import { getDashboard, getRefunds, getUsers } from "./api/adminApi";
import PolicyManagement from "./components/PolicyManagement";

export default function App({ keycloak }) {
  const [view, setView] = useState("dashboard");
  const [dashboard, setDashboard] = useState(null);
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function accessToken() {
    await keycloak.updateToken(30);
    return keycloak.token;
  }

  async function load(nextView = view, nextSearch = search) {
    try {
      setLoading(true);
      setError("");

      const token = await accessToken();

      if (nextView === "dashboard") {
        setDashboard(await getDashboard(token));
      }

      if (nextView === "users") {
        setRows(await getUsers(token, nextSearch));
      }

      if (nextView === "refunds") {
        setRows(await getRefunds(token, nextSearch));
      }
    }
    catch (loadError) {
      setError(loadError.message);
    }
    finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load("dashboard", "");
  }, []);

  function switchView(nextView) {
    setView(nextView);
    setSearch("");
    setRows([]);
    load(nextView, "");
  }

  return (
    <div className="shell">
      <aside className="sidebar">
        <div>
          <div className="brand">Refund Admin</div>
          <div className="subtitle">Operations Console</div>
        </div>

        {["dashboard", "users", "refunds", "policies", "system"].map(item => (
          <button
            className={view === item ? "nav active" : "nav"}
            key={item}
            onClick={() => switchView(item)}
            type="button"
          >
            {item[0].toUpperCase() + item.slice(1)}
          </button>
        ))}

        <button
          className="signout"
          onClick={() => keycloak.logout()}
          type="button"
        >
          Sign out
        </button>
      </aside>

      <main className="content">
        <header className="header">
          <div>
            <p className="eyebrow">ADMIN</p>
            <h1>{view[0].toUpperCase() + view.slice(1)}</h1>
          </div>

          {(view === "users" || view === "refunds") && (
            <form
              className="search"
              onSubmit={event => {
                event.preventDefault();
                load(view, search);
              }}
            >
              <input
                placeholder={`Search ${view}`}
                value={search}
                onChange={event => setSearch(event.target.value)}
              />
              <button type="submit">Search</button>
            </form>
          )}
        </header>

        {error && <section className="card error">{error}</section>}
        {loading && <section className="card">Loading...</section>}

        {view === "dashboard" && dashboard && !loading && (
          <>
            <section className="metrics">
              <article className="metric">
                <span>Users</span>
                <strong>{dashboard.totalUsers}</strong>
              </article>
              <article className="metric">
                <span>Tax returns</span>
                <strong>{dashboard.totalTaxReturns}</strong>
              </article>
              <article className="metric">
                <span>Refunds</span>
                <strong>{dashboard.totalRefunds}</strong>
              </article>
            </section>

            <section className="card">
              <h2>Refunds by status</h2>
              <div className="statusGrid">
                {Object.entries(dashboard.refundsByStatus || {}).map(([status, count]) => (
                  <div className="statusItem" key={status}>
                    <span>{status}</span>
                    <strong>{count}</strong>
                  </div>
                ))}
              </div>
            </section>
          </>
        )}

        {view === "users" && !loading && (
          <section className="card">
            <h2>Registered users</h2>
            <div className="table">
              {rows.map(user => (
                <article className="row" key={user.user_id}>
                  <div>
                    <strong>{user.display_name || user.email}</strong>
                    <small>{user.email}</small>
                  </div>
                  <span>{user.has_filed_return ? "Has return" : "No return"}</span>
                  <small>{user.created_at ? new Date(user.created_at).toLocaleString() : "Unknown"}</small>
                </article>
              ))}
            </div>
          </section>
        )}

        {view === "refunds" && !loading && (
          <section className="card">
            <h2>Refunds</h2>
            <div className="table">
              {rows.map(refund => (
                <article className="row refundRow" key={refund.tax_return_id}>
                  <div>
                    <strong>{refund.customer_name || refund.email}</strong>
                    <small>{refund.external_refund_id}</small>
                  </div>
                  <span className="pill">{refund.status}</span>
                  <span>{refund.refund_amount}</span>
                  <small>{refund.last_checked_at ? new Date(refund.last_checked_at).toLocaleString() : "Never"}</small>
                </article>
              ))}
            </div>
          </section>
        )}

        {view === "policies" && (
          <PolicyManagement keycloak={keycloak} />
        )}

        {view === "system" && (
          <section className="card placeholder">
            System operations are planned for a later release.
          </section>
        )}
      </main>
    </div>
  );
}
