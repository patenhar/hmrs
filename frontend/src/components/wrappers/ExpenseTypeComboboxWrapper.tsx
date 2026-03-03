import AsyncCombobox from "../Custom/AsyncCombobox";
import { useGetExpenseType } from "@/api/queries/useExpense";

export default function ExpenseTypeComboboxWrapper({ disabled, form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Expense Type"}
      placeholder={"Select expense type"}
      fetchFunction={useGetExpenseType}
      displayKey={"expenseTypeName"}
      primaryKey={"pkExpenseTypeId"}
    />
  );
}
