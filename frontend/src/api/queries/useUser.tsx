import {
  useMutation,
  useQuery,
  useQueryClient,
  keepPreviousData,
} from "@tanstack/react-query";
import authService from "../userService.tsx";
import type { UserPageParams } from "../userService.tsx";
import { toast } from "sonner";

const {
  getAllUsers,
  getUserById,
  getUsersByEmail,
  getUsersByGameAndName,
  getCurrentUser,
  getUsersPaginated,
  updateUserRole,
} = authService;

export const useGetAllUsers = () => {
  return useQuery({
    queryKey: ["Users"],
    queryFn: () => getAllUsers(),
  });
};

export const useGetUsersPaginated = (params: UserPageParams) => {
  return useQuery({
    queryKey: ["Users", "paginated", params],
    queryFn: () => getUsersPaginated(params),
    placeholderData: keepPreviousData,
  });
};

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
    enabled: email.trim().length > 0,
  });
};

export const useUsersByGameAndName = (gameId: string, name: string) => {
  return useQuery({
    queryKey: ["User", "GameInterest", gameId, name],
    queryFn: () => getUsersByGameAndName(gameId, name),
    enabled: !!gameId && name.trim().length > 0,
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

export const useUpdateUserRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateUserRole,
    onSuccess: (res) => {
      toast.success(res.data.message ?? "User role updated successfully");
      queryClient.invalidateQueries({ queryKey: ["Users"] });
      queryClient.invalidateQueries({ queryKey: ["CurrentUser"] });
    },
    onError: (error) => {
      toast.error("Role update failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
