import { useMutation, useQuery } from "@tanstack/react-query";
import { toast } from "sonner";
import profileService from "../profileService.tsx";

const { createProfile, getProfileById, getProfileByUserId, updateProfile } =
  profileService;

export const useGetProfileById = (id: string) => {
  return useQuery({
    queryKey: ["Profile", id],
    queryFn: () => getProfileById(id),
    enabled: !!id,
  });
};

export const useCreateProfile = () => {
  return useMutation({
    mutationFn: createProfile,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Profile creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useGetProfileByUserId = (userId: string, enabled = true) => {
  return useQuery({
    queryKey: ["Profile", "User", userId],
    queryFn: () => getProfileByUserId(userId),
    enabled: enabled && !!userId,
  });
};

export const useUpdateProfile = () => {
  return useMutation({
    mutationFn: updateProfile,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Profile update failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
