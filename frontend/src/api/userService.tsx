import axiosClient from "./axiosClient";

const userService = {
  getUserById: (id: string) => axiosClient.get(`/users/${id}`),
  getUsersByEmail: (email: string) =>
    axiosClient.get("/users/search", { params: { email } }),
  getCurrentUser: () => axiosClient.get("/users/me"),
};

export default userService;
