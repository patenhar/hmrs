import { useGetCountryByName } from "@/api/queries/useCountry";
import AsyncCombobox from "../Custom/AsyncCombobox";

export default function CountryComboboxWrapper({ disabled, form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Country"}
      placeholder={"Select Country"}
      fetchFunction={useGetCountryByName}
      displayKey={"countryName"}
      primaryKey={"pkCountryId"}
    />
  );
}
