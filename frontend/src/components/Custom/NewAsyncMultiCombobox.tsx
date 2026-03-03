import * as React from "react";
import {
  Combobox,
  ComboboxContent,
  ComboboxInput,
  ComboboxItem,
  ComboboxList,
  ComboboxStatus,
} from "../ui/combobox";
import { Controller, type UseFormReturn } from "react-hook-form";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Spinner } from "../ui/spinner";
import { useImperativeHandle, useState } from "react";
import useFetch from "@/hooks/useFetch";

interface NewAsyncMultiComboboxProps {
  form: UseFormReturn;
  name: string;
  label: string;
  placeholder?: string;
  fetchFunction: any;
  displayKey: string;
  primaryKey: string;
  ref: React.Ref<{ updateSelectedValues: () => void }>;
}

export default function NewAsyncMultiCombobox({
  form,
  name,
  label,
  placeholder = "",
  fetchFunction,
  ref,
  displayKey,
  primaryKey,
}: Readonly<NewAsyncMultiComboboxProps>) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading, data } = useFetch(fetchFunction, searchValue);

  const [searchResults, setSearchResults] = useState([]);
  const [selectedValues, setSelectedValues] = useState([]);
  useImperativeHandle(ref, () => ({
    updateSelectedValues: () => setSelectedValues(form.watch(name) ?? []),
  }));

  const trimmedSearchValue = (searchValue ?? "").trim();

  const items = React.useMemo(() => {
    if (selectedValues.length === 0) {
      return searchResults;
    }

    const merged = [...searchResults];

    selectedValues.forEach((item) => {
      if (
        searchResults.some((result) => result[primaryKey] === item[primaryKey])
      ) {
        const index = merged.findIndex(
          (result) => result[primaryKey] === item[primaryKey],
        );
        console.log("index", index);
        if (index !== -1) {
          merged[index] = item;
        }
      } else {
        merged.push(item);
      }
    });
    return merged;
  }, [searchResults, selectedValues, primaryKey]);

  function getStatus() {
    if (isLoading) {
      return (
        <div className="flex justify-center items-center gap-2">
          <Spinner />
          Searching...
        </div>
      );
    }

    if (trimmedSearchValue === "") {
      return selectedValues.length > 0 ? null : "Start typing to search...";
    }

    if (searchResults.length === 0) {
      return `No matches for "${trimmedSearchValue}".`;
    }

    return null;
  }

  return (
    <Controller
      name={name}
      control={form.control}
      render={({ field, fieldState }) => (
        <Field data-invalid={fieldState.invalid}>
          <FieldLabel htmlFor={name}>{label}</FieldLabel>
          <Combobox
            items={items}
            value={selectedValues}
            multiple
            itemToStringLabel={(item) => item[displayKey]}
            filter={null}
            onOpenChange={(open) => {
              if (!open || (open && trimmedSearchValue === "")) {
                setSearchResults(selectedValues);
              }
            }}
            onValueChange={(nextSelectedValues) => {
              setSearchValue("");
              setSelectedValues(nextSelectedValues);
              field.onChange(nextSelectedValues);
            }}
            onInputValueChange={(nextSearchValue, { reason }) => {
              const normalizedSearchValue = nextSearchValue ?? "";
              setSearchValue(normalizedSearchValue);

              if (normalizedSearchValue === "") {
                setSearchResults(selectedValues);

                return;
              }

              if (reason === "item-press") {
                return;
              }

              setSearchResults(data ?? []);
            }}
          >
            <ComboboxInput id={name} placeholder={placeholder} />

            <ComboboxContent
              className="pointer-events-auto"
              onClick={(e) => e.stopPropagation()}
            >
              <ComboboxStatus>{getStatus()}</ComboboxStatus>
              <ComboboxList className="overflow-y-auto">
                {(item) => (
                  <ComboboxItem key={item[primaryKey]} value={item}>
                    {item[displayKey]}
                  </ComboboxItem>
                )}
              </ComboboxList>
            </ComboboxContent>
          </Combobox>
          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
        </Field>
      )}
    />
  );
}
