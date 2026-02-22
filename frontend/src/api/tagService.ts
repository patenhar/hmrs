import axiosClient from "./axiosClient";

const tagService = {
  getAllTags: () => axiosClient.get("/tags/"),
  createTag: (tagName: string) =>
    axiosClient.post("/tags/", null, { params: { tagName } }),
};

export default tagService;
