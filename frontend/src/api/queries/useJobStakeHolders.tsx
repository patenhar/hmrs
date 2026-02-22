import { useQuery } from "@tanstack/react-query";
import authService from "../jobStakeHolderService.tsx";

const { getJobStakeHolderTypes } = authService;

export const useJobStakeHolderTypes = (name: string) => {
  return useQuery({
    queryKey: ["JobStakeHolderTypes", name],
    queryFn: () => getJobStakeHolderTypes(name),
  });
};
