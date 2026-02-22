import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";
import { useGetExpenseType } from "@/api/queries/useExpense";

export default function ExpenseTypeComboboxWrapper({ disabled, form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading, data } = useGetExpenseType(searchValue);

  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Expense Type"}
      placeholder={"Select expense type"}
      isLoading={isLoading}
      queryRes={data?.data.data}
      valueField={"pkExpenseTypeId"}
      displayField={"expenseTypeName"}
      onInputChange={setSearchValue}
    />
  );
}
