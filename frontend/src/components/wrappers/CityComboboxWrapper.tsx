import { useGetCityByCountry } from "@/api/queries/useCity";
import React, { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";

export default function CityComboboxWrapper({ disabled, form, name, idx }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: cityLoading, data: cityData } = useGetCityByCountry(
    searchValue,
    form.getValues(`destinations.${idx}.countryId`),
  );

  console.log(cityData);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"City"}
      placeholder={"Select city"}
      isLoading={cityLoading}
      queryRes={cityData?.data.data}
      valueField={"pkCityId"}
      displayField={"cityName"}
      onInputChange={setSearchValue}
    />
  );
}
