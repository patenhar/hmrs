import { useMutation, useQueryClient } from "@tanstack/react-query";
import authService from "../authService.tsx";
import { toast } from "sonner";

const { register, login } = authService;

export const useRegister = () => {
  return useMutation({
    mutationFn: register,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Registration failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useLogin = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: login,
    onSuccess: (res) => {
      sessionStorage.setItem("token", res.data.data.token);
      queryClient.invalidateQueries({ queryKey: ["CurrentUser"] });
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Login failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
