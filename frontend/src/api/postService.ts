import axiosClient from "./axiosClient";
import type { PostFilters, PostReqDto } from "./types/post.types";

const postService = {
  getAllPosts: (params?: PostFilters) => axiosClient.get("/posts/", { params }),
  getPostById: (id: string) => axiosClient.get(`/posts/${id}`),
  createPost: (data: PostReqDto) => axiosClient.post("/posts/", data),
  updatePost: ({ id, data }: { id: string; data: PostReqDto }) =>
    axiosClient.put(`/posts/${id}`, data),
  deletePost: ({ id, remarks }: { id: string; remarks?: string }) =>
    axiosClient.delete(`/posts/${id}`, { data: { remarks } }),
  toggleLike: (id: string) => axiosClient.post(`/posts/${id}/like`),
  triggerCelebrations: () => axiosClient.post("/posts/trigger-celebrations"),
};

export default postService;
