import axiosClient from "./axiosClient";

const departmentService = {
  getDepartments: (name: string) =>
    axiosClient.get("/departments/search", { params: { name } }),
};

export default departmentService;
