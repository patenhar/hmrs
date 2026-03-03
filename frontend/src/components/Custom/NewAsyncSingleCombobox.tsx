"use client";
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
import { useMemo, useState } from "react";
import useFetch from "@/hooks/useFetch";

interface NewAsyncSingleComboboxProps {
  form: UseFormReturn;
  name: string;
  label: string;
  placeholder?: string;
  fetchFunction: any;
  displayKey: string;
  primaryKey: string;
}

export default function NewAsyncSingleCombobox({
  form,
  name,
  label,
  placeholder = "",
  fetchFunction,
  displayKey,
  primaryKey,
}: Readonly<NewAsyncSingleComboboxProps>) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading, data } = useFetch(fetchFunction, searchValue);

  const [searchResults, setSearchResults] = useState([]);
  const [selectedValue, setSelectedValue] = useState(null);

  const trimmedSearchValue = (searchValue ?? "").trim();

  const items = useMemo(() => {
    if (
      !selectedValue ||
      searchResults.some(
        (item) => item[primaryKey] === selectedValue[primaryKey],
      )
    ) {
      return searchResults;
    }

    return [...searchResults, selectedValue];
  }, [searchResults, selectedValue, primaryKey]);

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
      return selectedValue ? null : "Start typing to search...";
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
            value={selectedValue}
            itemToStringLabel={(item) => item[displayKey]}
            filter={null}
            onOpenChangeComplete={(open) => {
              if (!open && selectedValue) {
                setSearchResults([selectedValue]);
              }
            }}
            onValueChange={(nextSelectedValue) => {
              setSearchValue("");
              setSelectedValue(nextSelectedValue);
              field.onChange(nextSelectedValue);
            }}
            onInputValueChange={(nextSearchValue, { reason }) => {
              const normalizedSearchValue = nextSearchValue ?? "";
              setSearchValue(normalizedSearchValue);

              if (normalizedSearchValue === "") {
                setSearchResults([]);
                return;
              }

              if (reason === "item-press") {
                return;
              }

              setSearchResults(data ?? []);
            }}
          >
            <ComboboxInput id={name} placeholder={placeholder} showClear />

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
