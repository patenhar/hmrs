import axiosClient from "./axiosClient";

const permissionService = {
  getAllPermissions: () => axiosClient.get("/permissions/"),
  addPermission: (data: { permissionName: string }) =>
    axiosClient.post("/permissions/", data),
};

export default permissionService;
