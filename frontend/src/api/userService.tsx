import axiosClient from "./axiosClient";

export interface UserPageParams {
  page: number;
  size: number;
  sort: string;
  email?: string;
  name?: string;
  "role-name"?: string;
}

const userService = {
  getAllUsers: () => axiosClient.get("/users/"),
  getUserById: (id: string) => axiosClient.get(`/users/${id}`),
  getUsersByEmail: (email: string) =>
    axiosClient.get("/users/search", { params: { email } }),
  getUsersByGameAndName: (gameId: string, name: string) =>
    axiosClient.get("/users/search/by-game", { params: { gameId, name } }),
  getCurrentUser: () => axiosClient.get("/users/me"),
  getUsersPaginated: (params: UserPageParams) =>
    axiosClient.get("/users/filtering&pagination&sorting", { params }),
  updateUserRole: ({ userId, roleId }: { userId: string; roleId: string }) =>
    axiosClient.patch(`/users/${userId}`, JSON.stringify(roleId), {
      headers: { "Content-Type": "application/json" },
    }),
};

export default userService;
