import AsyncCombobox from "../Custom/AsyncCombobox";
import { useJobStakeHolderTypes } from "@/api/queries/useJobStakeHolders";

export default function JobStakeHolderTypeComboboxWrapper({
  disabled,
  form,
  name,
}) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Job stake holder type"}
      placeholder={"Select Job stake holder type"}
      fetchFunction={useJobStakeHolderTypes}
      displayKey={"jobStakeHolderTypeName"}
      primaryKey={"pkJobStakeHolderTypeId"}
    />
  );
}
