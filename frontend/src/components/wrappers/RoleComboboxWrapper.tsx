import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";
import { useGetRoleByName } from "@/api/queries/useRole";

export default function RoleComboboxWrapper({ disabled, form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: roleLoading, data: roleData } =
    useGetRoleByName(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Role"}
      placeholder={"Select Role"}
      isLoading={roleLoading}
      queryRes={roleData?.data.data}
      valueField={"pkRoleId"}
      displayField={"roleName"}
      onInputChange={setSearchValue}
    />
  );
}
