const baseUrl =
  import.meta.env.VITE_ADMIN_API_URL || "http://localhost:8050";

async function request(path, token) {
  const response = await fetch(`${baseUrl}${path}`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  if (!response.ok) {
    throw new Error(
      (await response.text())
      || `Admin API failed with status ${response.status}`
    );
  }

  return response.json();
}

export const getDashboard = token =>
  request("/api/v1/admin/dashboard", token);

export const getUsers = (token, search) =>
  request(`/api/v1/admin/users?search=${encodeURIComponent(search || "")}`, token);

export const getRefunds = (token, search) =>
  request(`/api/v1/admin/refunds?search=${encodeURIComponent(search || "")}`, token);
