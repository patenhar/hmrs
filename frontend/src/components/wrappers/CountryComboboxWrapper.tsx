import { useGetCountryByName } from "@/api/queries/useCountry";
import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";

export default function CountryComboboxWrapper({ disabled, form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: countryLoading, data: CountryData } =
    useGetCountryByName(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Country"}
      placeholder={"Select Country"}
      isLoading={countryLoading}
      queryRes={CountryData?.data.data}
      valueField={"pkCountryId"}
      displayField={"countryName"}
      onInputChange={setSearchValue}
    />
  );
}
