import {
  useMutation,
  useQuery,
  useQueryClient,
  keepPreviousData,
} from "@tanstack/react-query";
import { useMemo } from "react";
import { toast } from "sonner";
import roleService from "../roleService.tsx";
import type { RolePageParams } from "../roleService.tsx";

const { getAllRoles, getRoleByName, getRolesPaginated, addRole, updateRole } =
  roleService;

export const useGetAllRoles = () => {
  return useQuery({
    queryKey: ["Role"],
    queryFn: () => getAllRoles(),
  });
};

export const useGetRolesPaginated = (params: RolePageParams) => {
  return useQuery({
    queryKey: ["Roles", "paginated", params],
    queryFn: () => getRolesPaginated(params),
    placeholderData: keepPreviousData,
  });
};

export const useSearchRoles = (search: string) => {
  const { data, isLoading, error } = useGetAllRoles();
  const allRoles = (data?.data?.data ?? []) as {
    pkRoleId: string;
    roleName: string;
  }[];
  const q = (search ?? "").trim().toLowerCase();

  const filtered = useMemo(
    () =>
      q
        ? allRoles.filter((r) => r.roleName.toLowerCase().includes(q))
        : allRoles,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [q, data],
  );

  const result = useMemo(
    () => ({ data: { data: { data: filtered } }, isLoading, error }),
    [filtered, isLoading, error],
  );

  return result;
};

export const useGetRoleByName = (name: string) => {
  return useQuery({
    queryKey: ["Role", name],
    queryFn: () => getRoleByName(name),
    enabled: name.length > 0,
  });
};

export const useAddRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addRole,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Role"] });
    },
    onError: (error) => {
      toast.error("Failed to add role", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useUpdateRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateRole,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Role"] });
    },
    onError: (error) => {
      toast.error("Failed to update role", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
