import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import commentService from "../commentService";
import type { CommentReqDto } from "../types/post.types";

const { getCommentsByPost, addComment, updateComment, deleteComment } =
  commentService;

export const useGetCommentsByPost = (postId: string) => {
  return useQuery({
    queryKey: ["Comment", postId],
    queryFn: () => getCommentsByPost(postId),
    enabled: !!postId,
  });
};

export const useAddComment = (postId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CommentReqDto) => addComment(data),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Comment", postId] });
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to add comment", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useUpdateComment = (postId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: CommentReqDto }) =>
      updateComment({ id, data }),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Comment", postId] });
    },
    onError: (error: Error) => {
      toast.error("Failed to update comment", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useDeleteComment = (postId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, remarks }: { id: string; remarks?: string }) =>
      deleteComment({ id, remarks }),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Comment", postId] });
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to delete comment", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
