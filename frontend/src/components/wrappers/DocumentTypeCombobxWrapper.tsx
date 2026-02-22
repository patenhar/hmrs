import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";
import { useGetDocumentTypes } from "@/api/queries/useDocument";

export default function DocumentTypeComboboxWrapper({ form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading, data } = useGetDocumentTypes(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Document type"}
      placeholder={"Select document type"}
      isLoading={isLoading}
      queryRes={data?.data.data}
      valueField={"pkDocumentTypeId"}
      displayField={"documentTypeName"}
      onInputChange={setSearchValue}
    />
  );
}
