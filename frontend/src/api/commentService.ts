import axiosClient from "./axiosClient";
import type { CommentReqDto } from "./types/post.types";

const commentService = {
  getCommentsByPost: (postId: string) =>
    axiosClient.get(`/comments/post/${postId}`),
  addComment: (data: CommentReqDto) => axiosClient.post("/comments/", data),
  updateComment: ({ id, data }: { id: string; data: CommentReqDto }) =>
    axiosClient.put(`/comments/${id}`, data),
  deleteComment: ({ id, remarks }: { id: string; remarks?: string }) =>
    axiosClient.delete(`/comments/${id}`, { data: { remarks } }),
};

export default commentService;
