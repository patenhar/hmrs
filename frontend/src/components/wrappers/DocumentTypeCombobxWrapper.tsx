import AsyncCombobox from "../Custom/AsyncCombobox";
import { useGetDocumentTypes } from "@/api/queries/useDocument";

export default function DocumentTypeComboboxWrapper({ form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Document type"}
      placeholder={"Select document type"}
      fetchFunction={useGetDocumentTypes}
      displayKey={"documentTypeName"}
      primaryKey={"pkDocumentTypeId"}
    />
  );
}
