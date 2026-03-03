import { useState } from "react";
import { useUser } from "@/api/queries/useUser";
import NewAsyncSingleCombobox from "../Custom/NewAsyncSingleCombobox";

export default function NewAsyncSingleWrapper({
  disabled,
  form,
  name,
  label,
  ref,
}) {
  const [searchValue, setSearchValue] = useState("");
  const { isLoading, data } = useUser(searchValue);

  return (
    <NewAsyncSingleCombobox
      form={form}
      name={name}
      label={label}
      searchValue={searchValue}
      setSearchValue={setSearchValue}
      isLoading={isLoading}
      data={data?.data.data ?? []}
      displayKey={"email"}
      primaryKey={"pkUserId"}
    />
  );
}
