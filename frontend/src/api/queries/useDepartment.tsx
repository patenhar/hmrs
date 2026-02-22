import { useQuery } from "@tanstack/react-query";
import departmentService from "../departmentService.tsx";

const { getDepartments } = departmentService;

export const useGetDepartments = (name: string) => {
  return useQuery({
    queryKey: ["Department", name],
    queryFn: () => getDepartments(name),
    enabled: name.length > 0,
  });
};
