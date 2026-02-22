import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";
import profileService from "../profileService.tsx";

const { createProfile } = profileService;

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
