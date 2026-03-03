import { useUser } from "@/api/queries/useUser";
import AsyncCombobox from "../Custom/AsyncCombobox";

export default function UserComboboxWrapper({ disabled, form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"User"}
      placeholder={"Select User"}
      fetchFunction={useUser}
      displayKey={"email"}
      primaryKey={"pkUserId"}
    />
  );
}
