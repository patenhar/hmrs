import axiosClient from "./axiosClient";

const travelService = {
  getAllTravels: () => axiosClient.get("/travels/"),
  getTravelById: (id: string) => axiosClient.get(`/travels/${id}`),
  createTravel: (travelData) => axiosClient.post("/travels/", travelData),
  updateTravel: (travelData) => axiosClient.put("/travels", travelData),
  deleteTravel: (id) => axiosClient.delete(`/travels/${id}`),
  getTravelUsers: (id: string) => axiosClient.get(`/travels/${id}/users`),
  getUserTravelDocuments: (id: string) =>
    axiosClient.get(`/travels/users/${id}/documents`),
  uploadTravelDocument: ({ id, documentDto }) =>
    axiosClient.post(`/travels/users/${id}/documents/upload`, documentDto),
  deleteTravelDocument: (id) =>
    axiosClient.delete(`/travels/users/${id}/documents`),
};

export default travelService;
