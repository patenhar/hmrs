import { useMutation, useQueryClient } from "@tanstack/react-query";
import authService from "../authService.tsx";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";

export const useLogout = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { setToken } = useAuth();
  return () => {
    setToken(null);
    queryClient.clear();
    navigate("/login");
    toast.success("Logged out successfully");
  };
};

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
  const navigate = useNavigate();
  const { setToken } = useAuth();
  return useMutation({
    mutationFn: login,
    onSuccess: (res) => {
      setToken(res.data.data.token);
      queryClient.invalidateQueries({ queryKey: ["CurrentUser"] });
      queryClient.invalidateQueries({ queryKey: ["Notifications"] });
      toast.success(res.data.message);
      navigate("/travels");
    },
    onError: (error) => {
      toast.error("Login failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
