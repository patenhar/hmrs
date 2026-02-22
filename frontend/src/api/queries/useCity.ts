import { useQuery } from "@tanstack/react-query";
import cityService from "../cityService.tsx";

const { getCityByCountry } = cityService;

export const useGetCityByCountry = (name: string, countryId: string) => {
  return useQuery({
    queryKey: ["City", name],
    queryFn: () => getCityByCountry(name, countryId),
    enabled: () => {
      return countryId && name.length > 0;
    },
  });
};
