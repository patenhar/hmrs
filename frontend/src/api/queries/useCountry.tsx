import { useQuery } from "@tanstack/react-query";
import countryService from "../countryService.tsx";

const { getCountryByName } = countryService;

export const useGetCountryByName = (name: string) => {
  return useQuery({
    queryKey: ["Country", name],
    queryFn: () => getCountryByName(name),
    enabled: name.length > 0,
  });
};
