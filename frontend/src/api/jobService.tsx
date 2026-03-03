import axiosClient from "./axiosClient";

const jobService = {
  getAllJobs: () => axiosClient.get("/jobs/"),
  getJobById: (id) => axiosClient.get(`/jobs/${id}`),
  addJob: (jobReqDto) => axiosClient.post("/jobs/", jobReqDto),
  updateJob: ({ jobId, data }) =>
    axiosClient.patch(`/jobs/${jobId}`, data, {
      headers: { "Content-Type": "multipart/form-data" },
    }),
  shareJob: ({ id, data }) => axiosClient.post(`/jobs/${id}/share`, data),
  referJob: ({ id, data }) => axiosClient.post(`/jobs/${id}/refer`, data),
  deleteJob: (id) => axiosClient.delete(`/jobs/${id}`),
};

export default jobService;
