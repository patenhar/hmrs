import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import permissionService from "../permissionService";

const { getAllPermissions, addPermission } = permissionService;

export const useGetAllPermissions = () => {
  return useQuery({
    queryKey: ["Permission"],
    queryFn: () => getAllPermissions(),
  });
};

export const useAddPermission = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addPermission,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Permission"] });
    },
    onError: (error) => {
      toast.error("Failed to add permission", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
