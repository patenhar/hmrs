import axiosClient from "./axiosClient";

const postVisibilityService = {
  getAllVisibilities: () => axiosClient.get("/post-visibilities/"),
};

export default postVisibilityService;
