import axiosClient from "./axiosClient";

const countryService = {
  getCountryByName: (name: string) =>
    axiosClient.get("/countries/search", { params: { name } }),
};

export default countryService;
