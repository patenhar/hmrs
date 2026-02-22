import axiosClient from "./axiosClient";

const cityService = {
  getCityByCountry: (name: string, countryId: string) =>
    axiosClient.get("/cities/search", { params: { name, countryId } }),
};

export default cityService;
