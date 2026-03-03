import axiosClient from "./axiosClient";

export interface TravelPageParams {
  page: number;
  size: number;
  sort: string;
  title?: string;
  description?: string;
  "travel-date"?: string;
  "return-date"?: string;
  "grant-limit-min"?: string;
  "grant-limit-max"?: string;
  "hr-mail"?: string;
}

const travelService = {
  getAllTravels: () => axiosClient.get("/travels/"),
  getTravelsPaginated: (params: TravelPageParams) =>
    axiosClient.get("/travels/filtering&pagination&sorting", { params }),
  getTravelById: (id: string) => axiosClient.get(`/travels/${id}`),
  createTravel: (travelData) => axiosClient.post("/travels/", travelData),
  updateTravel: ({ travelId, ...travelData }) =>
    axiosClient.put(`/travels/${travelId}`, travelData),
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
