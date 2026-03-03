import AsyncCombobox from "../Custom/AsyncCombobox";
import { useGetRoleByName } from "@/api/queries/useRole";

export default function RoleComboboxWrapper({ disabled, form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Role"}
      placeholder={"Select Role"}
      fetchFunction={useGetRoleByName}
      displayKey={"roleName"}
      primaryKey={"pkRoleId"}
    />
  );
}
