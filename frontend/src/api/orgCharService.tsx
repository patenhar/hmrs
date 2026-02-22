import axiosClient from "./axiosClient";

const orgChartService = {
  getOrgChart: (id: string) =>
    axiosClient.get(`/profile/${id}/org-chart`),
};

export default orgChartService;
