const baseUrl =
  import.meta.env.VITE_POLICY_MANAGEMENT_API_URL
  || "http://localhost:8040";

async function parse(response) {
  if (response.status === 204) return null;

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    throw new Error(
      typeof body === "string"
        ? body
        : body?.detail || body?.message || body?.error
          || `Request failed with status ${response.status}`
    );
  }

  return body;
}

export async function listPolicies(token) {
  return parse(await fetch(`${baseUrl}/api/v1/admin/policies`, {
    headers: { Authorization: `Bearer ${token}` }
  }));
}

export async function uploadPolicy(token, file) {
  const form = new FormData();
  form.append("file", file);

  return parse(await fetch(`${baseUrl}/api/v1/admin/policies`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
    body: form
  }));
}

export async function reindexPolicy(token, id) {
  return parse(await fetch(`${baseUrl}/api/v1/admin/policies/${id}/reindex`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` }
  }));
}

export async function deletePolicy(token, id) {
  return parse(await fetch(`${baseUrl}/api/v1/admin/policies/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` }
  }));
}
