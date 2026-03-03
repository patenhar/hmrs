import { useGetCityByCountry } from "@/api/queries/useCity";
import AsyncCombobox from "../Custom/AsyncCombobox";

export default function CityComboboxWrapper({ disabled, form, name, idx }) {
  const countryValue = form.watch(`destinations.${idx}.countryId`);
  const countryId =
    typeof countryValue === "object"
      ? (countryValue?.pkCountryId ?? "")
      : (countryValue ?? "");

  const fetchCity = (search: string) => useGetCityByCountry(search, countryId);

  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"City"}
      placeholder={countryId ? "Select city" : "Select country first"}
      fetchFunction={fetchCity}
      displayKey={"cityName"}
      primaryKey={"pkCityId"}
    />
  );
}
