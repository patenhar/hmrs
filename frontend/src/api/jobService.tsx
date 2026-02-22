import axiosClient from "./axiosClient";

const jobService = {
  getAllJobs: () => axiosClient.get("/jobs/"),
  getJobById: (id) => axiosClient.get(`/jobs/${id}`),
  addJob: (jobReqDto) => axiosClient.post("/jobs/", jobReqDto),
  shareJob: ({ id, data }) => axiosClient.post(`/jobs/${id}/share`, data),
  referJob: ({ id, data }) => axiosClient.post(`/jobs/${id}/refer`, data),
};

export default jobService;
