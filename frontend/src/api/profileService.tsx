import axiosClient from "./axiosClient";


const profileService = {
  createProfile: (profileData) =>
    axiosClient.post("/profiles/", profileData),
};

export default profileService;
