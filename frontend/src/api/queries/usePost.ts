import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import postService from "../postService";
import type { PostFilters, PostReqDto } from "../types/post.types";

const {
  getAllPosts,
  getPostById,
  createPost,
  updatePost,
  deletePost,
  toggleLike,
  triggerCelebrations,
} = postService;

export const useGetAllPosts = (filters?: PostFilters) => {
  return useQuery({
    queryKey: ["Post", filters],
    queryFn: () => getAllPosts(filters),
  });
};

export const useGetPostById = (id: string) => {
  return useQuery({
    queryKey: ["Post", id],
    queryFn: () => getPostById(id),
    enabled: !!id,
  });
};

export const useCreatePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: PostReqDto) => createPost(data),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to create post", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useUpdatePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: PostReqDto }) =>
      updatePost({ id, data }),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to update post", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useDeletePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, remarks }: { id: string; remarks?: string }) =>
      deletePost({ id, remarks }),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to delete post", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useTriggerCelebrations = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => triggerCelebrations(),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to generate celebration posts", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useToggleLike = (postId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => toggleLike(postId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["Post"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to toggle like", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
