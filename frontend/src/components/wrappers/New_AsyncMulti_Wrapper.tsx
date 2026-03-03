import { useImperativeHandle, useRef, useState } from "react";
import NewAsyncMultiCombobox from "../Custom/NewAsyncMultiCombobox";
import { useUser } from "@/api/queries/useUser";
import { se } from "date-fns/locale";

export default function New_AsyncMulti_Wrapper({
  disabled,
  form,
  name,
  label,
  ref,
}) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading, data } = useUser(searchValue);

  return (
    <NewAsyncMultiCombobox
     
    />
  );
}
