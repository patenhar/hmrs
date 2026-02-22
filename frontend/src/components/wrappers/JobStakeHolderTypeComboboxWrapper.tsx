import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";
import { useJobStakeHolderTypes } from "@/api/queries/useJobStakeHolders";

export default function JobStakeHolderTypeComboboxWrapper({
  disabled,
  form,
  name,
}) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: JobStakeHolderTypeLoading, data: JobStakeHolderTypeData } =
    useJobStakeHolderTypes(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Job stake holder type"}
      placeholder={"Select Job stake holder type"}
      isLoading={JobStakeHolderTypeLoading}
      queryRes={JobStakeHolderTypeData?.data.data}
      valueField={"pkJobStakeHolderTypeId"}
      displayField={"jobStakeHolderTypeName"}
      onInputChange={setSearchValue}
    />
  );
}
