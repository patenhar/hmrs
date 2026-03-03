import type { ColumnFiltersState } from "@tanstack/react-table";
import { PlusCircle, X } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export interface RangeValue {
  min: string;
  max: string;
}

export interface FilterableColumn {
  id: string;
  label: string;
  placeholder?: string;
  type?: "text" | "range";
}

interface FilterBuilderProps {
  readonly filterableColumns: FilterableColumn[];
  readonly columnFilters: ColumnFiltersState;
  readonly onFiltersChange: (filters: ColumnFiltersState) => void;
}

export function FilterBuilder({
  filterableColumns,
  columnFilters,
  onFiltersChange,
}: FilterBuilderProps) {
  const addFilter = () => {
    const usedIds = new Set(columnFilters.map((f) => f.id));
    const next = filterableColumns.find((c) => !usedIds.has(c.id));
    if (!next) return;
    const initial = next.type === "range" ? { min: "", max: "" } : "";
    onFiltersChange([...columnFilters, { id: next.id, value: initial }]);
  };

  const updateValue = (index: number, value: string | RangeValue) => {
    onFiltersChange(
      columnFilters.map((f, i) => (i === index ? { ...f, value } : f)),
    );
  };

  const changeColumn = (index: number, newId: string) => {
    const newCol = filterableColumns.find((c) => c.id === newId);
    const initial = newCol?.type === "range" ? { min: "", max: "" } : "";
    onFiltersChange(
      columnFilters.map((f, i) =>
        i === index ? { id: newId, value: initial } : f,
      ),
    );
  };

  const removeFilter = (index: number) => {
    onFiltersChange(columnFilters.filter((_, i) => i !== index));
  };

  const canAddMore = columnFilters.length < filterableColumns.length;

  return (
    <div className="flex flex-wrap items-center gap-2">
      {columnFilters.map((filter, index) => {
        const col = filterableColumns.find((c) => c.id === filter.id);
        const isRange = col?.type === "range";
        const rangeVal = filter.value as RangeValue;
        const textVal = filter.value as string;

        const usedElsewhere = new Set(
          columnFilters.filter((_, i) => i !== index).map((f) => f.id),
        );

        return (
          <div
            key={`${filter.id}-${String(index)}`}
            className="flex items-center gap-1.5 rounded-md border px-2 py-1 text-sm"
          >
            <Select
              value={filter.id}
              onValueChange={(v) => changeColumn(index, v)}
            >
              <SelectTrigger
                size="sm"
                className="h-6 w-auto border-0 bg-transparent p-0 text-xs font-semibold shadow-none focus-visible:ring-0"
              >
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {filterableColumns.map((c) => (
                  <SelectItem
                    key={c.id}
                    value={c.id}
                    disabled={usedElsewhere.has(c.id)}
                  >
                    {c.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            {isRange ? (
              <>
                <Input
                  type="number"
                  value={rangeVal?.min ?? ""}
                  onChange={(e) =>
                    updateValue(index, {
                      min: e.target.value,
                      max: rangeVal?.max ?? "",
                    })
                  }
                  placeholder="Min"
                  className="h-6 w-16 border-0 bg-transparent p-0 text-xs shadow-none focus-visible:ring-0"
                />
                <Input
                  type="number"
                  value={rangeVal?.max ?? ""}
                  onChange={(e) =>
                    updateValue(index, {
                      min: rangeVal?.min ?? "",
                      max: e.target.value,
                    })
                  }
                  placeholder="Max"
                  className="h-6 w-16 border-0 bg-transparent p-0 text-xs shadow-none focus-visible:ring-0"
                />
              </>
            ) : (
              <Input
                value={textVal}
                onChange={(e) => updateValue(index, e.target.value)}
                placeholder={col?.placeholder ?? "Value…"}
                className="h-6 w-28 border-0 bg-transparent p-0 text-xs shadow-none focus-visible:ring-0"
              />
            )}

            <button
              type="button"
              onClick={() => removeFilter(index)}
              aria-label={`Remove ${col?.label ?? filter.id} filter`}
              className="text-muted-foreground hover:text-foreground transition-colors"
            >
              <X className="h-3.5 w-3.5" />
            </button>
          </div>
        );
      })}

      {canAddMore && (
        <Button
          variant="outline"
          size="sm"
          className="h-8 gap-1.5 text-xs"
          onClick={addFilter}
        >
          <PlusCircle className="h-3.5 w-3.5" />
          Add Filter
        </Button>
      )}

      {columnFilters.length > 0 && (
        <Button
          variant="ghost"
          size="sm"
          className="h-8 text-xs text-muted-foreground"
          onClick={() => onFiltersChange([])}
        >
          Clear all
        </Button>
      )}
    </div>
  );
}
