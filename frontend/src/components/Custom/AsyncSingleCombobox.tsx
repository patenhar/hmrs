import {
  Combobox,
  ComboboxContent,
  ComboboxInput,
  ComboboxItem,
  ComboboxList,
} from "@/components/ui/combobox";
import { useEffect, useState, useTransition } from "react";
import { Combobox as ComboboxBase } from "@base-ui/react/combobox";
import { Controller } from "react-hook-form";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";

export function AsyncSingleCombobox({
  disabled,
  name,
  form,
  label,
  placeholder,
  isLoading,
  queryRes,
  valueField,
  displayField,
  onInputChange,
}) {
  const [searchValue, setSearchValue] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [isPending, startTransition] = useTransition();

  const trimmedSearchValue = searchValue?.trim();

  function getStatus() {
    if (isLoading || isPending) {
      return "Searching";
    }

    if (!trimmedSearchValue) {
      return "Start typing to search...";
    }

    if (searchResults.length === 0) {
      return `No matches for "${trimmedSearchValue}". Try a different search term.`;
    }

    return null;
  }

  return (
    <Controller
      name={name}
      control={form.control}
      render={({ field, fieldState }) => {
        const selectedItem =
          searchResults.find((item) => item[valueField] === field.value) ??
          null;
        return (
          <Field data-invalid={fieldState.invalid}>
            <FieldLabel htmlFor={name}>{label}</FieldLabel>
            <Combobox
              id={name}
              value={selectedItem}
              items={searchResults}
              itemToStringLabel={(item) => item[displayField]}
              onValueChange={(nextSelectedValue) => {
                field.onChange(
                  nextSelectedValue ? nextSelectedValue[valueField] : null,
                );
                setSearchValue("");
              }}
              onInputValueChange={(nextSearchValue) => {
                setSearchValue(nextSearchValue);
                if (!nextSearchValue) {
                  setSearchResults([]);
                  return;
                }
                startTransition(() => {
                  onInputChange(nextSearchValue);
                  setSearchResults(queryRes ?? []);
                });
              }}
            >
              <ComboboxInput
                placeholder={placeholder}
                showClear
                disabled={disabled}
              />
              <ComboboxContent
                className="pointer-events-auto"
                onClick={(e) => e.stopPropagation()}
              >
                <ComboboxBase.Status className="text-muted-foreground hidden w-full justify-center py-2 text-center text-sm group-data-empty/combobox-content:flex">
                  {getStatus()}
                </ComboboxBase.Status>
                <ComboboxList>
                  {(item) => (
                    <ComboboxItem key={String(item[valueField])} value={item}>
                      {String(item[displayField])}
                    </ComboboxItem>
                  )}
                </ComboboxList>
              </ComboboxContent>
            </Combobox>
            {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
          </Field>
        );
      }}
    />
  );
}
