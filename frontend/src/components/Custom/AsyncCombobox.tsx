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
import {
  useCallback,
  useEffect,
  useImperativeHandle,
  useMemo,
  useState,
} from "react";
import useFetch from "@/hooks/useFetch";
import useDebounce from "@/hooks/useDebounce";

interface AsyncComboboxProps {
  single?: boolean;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  form: UseFormReturn<any>;
  name: string;
  label: string;
  placeholder?: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  fetchFunction: any;
  ref?: React.Ref<{ updateSelectedValues: () => void }>;
  displayKey: string;
  primaryKey: string;
}

export default function AsyncCombobox({
  single = true,
  form,
  name,
  label,
  placeholder = "",
  fetchFunction,
  ref,
  displayKey,
  primaryKey,
}: Readonly<AsyncComboboxProps>) {
  const [searchValue, setSearchValue] = useState("");
  const { debouncedValue, isLoading: isDebouncing } = useDebounce({
    value: searchValue,
    delay: 300,
  });

  const { data, isLoading } = useFetch(fetchFunction, debouncedValue);

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [searchResults, setSearchResults] = useState<any[]>([]);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [selectedValue, setSelectedValue] = useState<any[]>([]);

  useImperativeHandle(ref, () => ({
    updateSelectedValues: () => setSelectedValue(form.watch(name) ?? []),
  }));

  const watchedValue = form.watch(name);
  useEffect(() => {
    if (single) {
      const currentPk = selectedValue.at(0)?.[primaryKey];
      const newPk = watchedValue?.[primaryKey];
      if (currentPk !== newPk) {
        setSelectedValue(watchedValue ? [watchedValue] : []);
      }
    } else {
      const watched = Array.isArray(watchedValue) ? watchedValue : [];
      const currentPks = selectedValue.map((i) => i[primaryKey]).join(",");
      const newPks = watched.map((i: any) => i[primaryKey]).join(",");
      if (currentPks !== newPks) {
        setSelectedValue(watched);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [watchedValue, primaryKey, single]);

  const trimmedSearchValue = (searchValue ?? "").trim();

  useEffect(() => {
    if (!isDebouncing && !isLoading) {
      if (trimmedSearchValue === "") {
        // eslint-disable-next-line react-compiler/react-compiler
        setSearchResults([]);
        return;
      }

      setSearchResults(data ?? []);
    }
  }, [data, isDebouncing, isLoading, trimmedSearchValue]);

  const forSingle = useCallback(() => {
    if (
      selectedValue.length == 0 ||
      searchResults.some(
        (item) => item[primaryKey] === selectedValue.at(0)?.[primaryKey],
      )
    ) {
      return searchResults;
    }

    return [...searchResults, ...selectedValue];
  }, [searchResults, selectedValue, primaryKey]);

  const forMultiple = useCallback(() => {
    if (selectedValue.length === 0) {
      return searchResults;
    }

    const merged = [...searchResults];

    selectedValue.forEach((item) => {
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
  }, [searchResults, selectedValue, primaryKey]);

  const items = useMemo(() => {
    return single ? forSingle() : forMultiple();
  }, [single, forSingle, forMultiple]);

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
      return "Start typing to search...";
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
          {label && <FieldLabel htmlFor={name}>{label}</FieldLabel>}
          <Combobox
            items={items}
            value={single ? (selectedValue.at(0) ?? null) : selectedValue}
            multiple={!single}
            itemToStringLabel={(item) => item[displayKey]}
            filter={null}
            onOpenChangeComplete={(open) => {
              if (!open || (open && trimmedSearchValue === "")) {
                setSearchResults(selectedValue);
              }
            }}
            onValueChange={(nextSelectedValue) => {
              setSearchValue("");
              if (single) {
                setSelectedValue(nextSelectedValue ? [nextSelectedValue] : []);
                field.onChange(nextSelectedValue);
              } else {
                setSelectedValue(nextSelectedValue);
                field.onChange(nextSelectedValue);
              }
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
            }}
          >
            <ComboboxInput
              id={name}
              placeholder={placeholder}
              showClear={single}
            />

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
