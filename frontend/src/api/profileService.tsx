import axiosClient from "./axiosClient";

const profileService = {
  createProfile: (profileData) => axiosClient.post("/profiles/", profileData),
  getProfileById: (id: string) => axiosClient.get(`/profiles/${id}`),
  getProfileByUserId: (userId: string) =>
    axiosClient.get(`/profiles/users/${userId}`),
  updateProfile: ({ profileId, data }) =>
    axiosClient.patch(`/profiles/${profileId}`, data),
};

export default profileService;
