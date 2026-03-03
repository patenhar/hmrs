import axios from "axios";

const axiosClient = axios.create({
  baseURL: import.meta.env.API_BASE_URL || "http://localhost:8080/api",
});

axiosClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;

  return config;
});

axiosClient.interceptors.response.use(
  (res) => res,
  (err) => {
    const message = err.response?.data?.message || "Something went wrong";
    const error = new Error(message) as Error & {
      errors?: { field: string; message: string }[];
    };
    const rawErrors = err.response?.data?.data?.errors;
    if (Array.isArray(rawErrors)) {
      error.errors = rawErrors;
    }
    return Promise.reject(error);
  },
);

export default axiosClient;
