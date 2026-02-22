import { useQuery } from "@tanstack/react-query";
import authService from "../userService.tsx";

const { getUserById, getUsersByEmail, getCurrentUser } = authService;

export const GetUserById = (id: string) => {
  return useQuery({
    queryKey: ["User", id],
    queryFn: () => getUserById(id),
    enabled: id.length > 0,
  });
};

export const useUser = (email: string) => {
  return useQuery({
    queryKey: ["User", email],
    queryFn: () => getUsersByEmail(email),
  });
};

export const useGetCurrentUser = () => {
  return useQuery({
    queryKey: ["CurrentUser"],
    queryFn: () => getCurrentUser(),
    enabled: !!sessionStorage.getItem("token"),
    staleTime: 5 * 60 * 1000,
    retry: false,
  });
};
