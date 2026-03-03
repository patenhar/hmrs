import { useQuery } from "@tanstack/react-query";
import orgChartService from "../orgCharService.tsx";

const { getOrgChart } = orgChartService;

export const useGetOrgChart = (id: string) => {
  return useQuery({
    queryKey: ["OrgChart", id],
    queryFn: () => getOrgChart(id),
    enabled: !!id,
  });
};
