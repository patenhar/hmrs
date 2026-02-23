import axiosClient from "./axiosClient";

const roleService = {
  getRoleByName: (name: string) =>
    axiosClient.get("/roles/search", { params: { roleName: name } }),
};

export default roleService;
