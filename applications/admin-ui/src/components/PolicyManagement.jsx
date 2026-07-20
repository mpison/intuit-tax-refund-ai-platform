import React, { useEffect, useState } from "react";
import {
  deletePolicy,
  listPolicies,
  reindexPolicy,
  uploadPolicy
} from "../api/policyApi";

export default function PolicyManagement({ keycloak }) {
  const [policies, setPolicies] = useState([]);
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function token() {
    await keycloak.updateToken(30);
    return keycloak.token;
  }

  async function load() {
    try {
      setLoading(true);
      setError("");
      setPolicies(await listPolicies(await token()));
    }
    catch (loadError) {
      setError(loadError.message);
    }
    finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function submit(event) {
    event.preventDefault();
    if (!file) return;

    try {
      setSaving(true);
      setMessage("");
      setError("");
      await uploadPolicy(await token(), file);
      event.target.reset();
      setFile(null);
      setMessage("Policy uploaded and ingestion completed.");
      await load();
    }
    catch (uploadError) {
      setError(uploadError.message);
    }
    finally {
      setSaving(false);
    }
  }

  async function reindex(policy) {
    try {
      setMessage("");
      setError("");
      await reindexPolicy(await token(), policy.policyDocumentId);
      setMessage("Policy re-index completed.");
      await load();
    }
    catch (reindexError) {
      setError(reindexError.message);
    }
  }

  async function remove(policy) {
    if (!window.confirm(`Delete ${policy.fileName}?`)) return;

    try {
      setMessage("");
      setError("");
      await deletePolicy(await token(), policy.policyDocumentId);
      setMessage("Policy deleted.");
      await load();
    }
    catch (deleteError) {
      setError(deleteError.message);
    }
  }

  return (
    <div className="policyPage">
      {error && <section className="card error">{error}</section>}
      {message && <section className="card success">{message}</section>}

      <section className="card">
        <h2>Upload policy</h2>
        <p className="muted">PDF, DOCX, or TXT. Maximum 20 MB.</p>

        <form className="policyUploadForm" onSubmit={submit}>
          <input
            accept=".pdf,.docx,.txt"
            required
            type="file"
            onChange={event => setFile(event.target.files?.[0] || null)}
          />
          <button disabled={saving || !file} type="submit">
            {saving ? "Uploading..." : "Upload and ingest"}
          </button>
        </form>
      </section>

      <section className="card">
        <div className="sectionHeader">
          <div>
            <h2>Policy documents</h2>
            <p className="muted">{policies.length} documents</p>
          </div>
          <button className="secondaryButton" onClick={load} type="button">
            Refresh
          </button>
        </div>

        {loading ? (
          <p>Loading policies...</p>
        ) : (
          <div className="policyTable">
            {policies.map(policy => (
              <article className="policyRow" key={policy.policyDocumentId}>
                <div>
                  <strong>{policy.fileName}</strong>
                  <small>{formatBytes(policy.fileSize)} · {policy.contentType || "Unknown"}</small>
                </div>

                <span className={`policyStatus status-${policy.status}`}>
                  {policy.status}
                </span>

                <div className="policyStats">
                  <span>Chunks<strong>{policy.chunkCount}</strong></span>
                  <span>Embeddings<strong>{policy.embeddingCount}</strong></span>
                </div>

                <small>
                  {policy.lastIngestedAt
                    ? new Date(policy.lastIngestedAt).toLocaleString()
                    : "Not ingested"}
                </small>

                <div className="policyActions">
                  <button className="secondaryButton" onClick={() => reindex(policy)} type="button">
                    Re-index
                  </button>
                  <button className="dangerButton" onClick={() => remove(policy)} type="button">
                    Delete
                  </button>
                </div>

                {policy.errorMessage && (
                  <p className="policyError">{policy.errorMessage}</p>
                )}
              </article>
            ))}

            {policies.length === 0 && <p>No policy documents uploaded.</p>}
          </div>
        )}
      </section>
    </div>
  );
}

function formatBytes(bytes) {
  if (!bytes) return "0 B";

  const units = ["B", "KB", "MB", "GB"];
  const index = Math.min(
    Math.floor(Math.log(bytes) / Math.log(1024)),
    units.length - 1
  );

  return `${(bytes / Math.pow(1024, index)).toFixed(index === 0 ? 0 : 1)} ${units[index]}`;
}
