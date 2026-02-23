import { useQuery } from "@tanstack/react-query";
import roleService from "../roleService.tsx";

const { getRoleByName } = roleService;

export const useGetRoleByName = (name: string) => {
  return useQuery({
    queryKey: ["Role", name],
    queryFn: () => getRoleByName(name),
    enabled: name.length > 0,
  });
};
