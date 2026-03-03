import axiosClient from "./axiosClient";

export interface RolePageParams {
  page: number;
  size: number;
  sort: string;
  "role-name"?: string;
}

const roleService = {
  getAllRoles: () => axiosClient.get("/roles/"),
  getRoleByName: (name: string) =>
    axiosClient.get("/roles/search", { params: { roleName: name } }),
  getRolesPaginated: (params: RolePageParams) =>
    axiosClient.get("/roles/filtering&pagination&sorting", { params }),
  addRole: (data: { roleName: string; permissionIds: string[] }) =>
    axiosClient.post("/roles/", data),
  updateRole: ({
    roleId,
    data,
  }: {
    roleId: string;
    data: { roleName: string; permissionIds: string[] };
  }) => axiosClient.patch(`/roles/${roleId}`, data),
};

export default roleService;
