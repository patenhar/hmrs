import { useUser } from "@/api/queries/useUser";
import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";

export default function UserComboboxWrapper({ disabled, form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: UserLoading, data: UserData } = useUser(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"User"}
      placeholder={"Select User"}
      isLoading={UserLoading}
      queryRes={UserData?.data.data}
      valueField={"pkUserId"}
      displayField={"email"}
      onInputChange={setSearchValue}
    />
  );
}
