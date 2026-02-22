import axiosClient from "./axiosClient";

const jobStakeHolderService = {
  getJobStakeHolderTypes: (name: string) =>
    axiosClient.get("/stakeholder-types/search", { params: { name } }),
};

export default jobStakeHolderService;
