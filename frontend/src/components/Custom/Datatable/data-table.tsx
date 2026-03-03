"use client";

import {
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";
import type {
  ColumnDef,
  ColumnFiltersState,
  OnChangeFn,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import {
  ArrowDown,
  ArrowUp,
  ArrowUpDown,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

interface DataTableProps<TData, TValue> {
  readonly columns: ColumnDef<TData, TValue>[];
  readonly data: TData[];
  readonly pageCount?: number;
  readonly pagination?: PaginationState;
  readonly onPaginationChange?: OnChangeFn<PaginationState>;
  readonly sorting?: SortingState;
  readonly onSortingChange?: OnChangeFn<SortingState>;
  readonly columnFilters?: ColumnFiltersState;
  readonly onColumnFiltersChange?: OnChangeFn<ColumnFiltersState>;
  readonly isLoading?: boolean;
  readonly onRowClick?: (row: TData) => void;
  readonly pageSizeOptions?: number[];
}

function SortIcon({ sorted }: { readonly sorted: false | "asc" | "desc" }) {
  if (sorted === "asc") return <ArrowUp className="h-3 w-3" />;
  if (sorted === "desc") return <ArrowDown className="h-3 w-3" />;
  return <ArrowUpDown className="h-3 w-3 opacity-50" />;
}

function TableBodyContent<TData, TValue>({
  table,
  columns,
  onRowClick,
}: {
  readonly table: ReturnType<typeof useReactTable<TData>>;
  readonly columns: ColumnDef<TData, TValue>[];
  readonly onRowClick?: (row: TData) => void;
}) {
  if (!table.getRowModel().rows?.length) {
    return (
      <TableRow>
        <TableCell colSpan={columns.length} className="h-24 text-center">
          No results.
        </TableCell>
      </TableRow>
    );
  }
  return table.getRowModel().rows.map((row) => (
    <TableRow
      key={row.id}
      data-state={row.getIsSelected() && "selected"}
      onClick={onRowClick ? () => onRowClick(row.original) : undefined}
      className={onRowClick ? "cursor-pointer" : ""}
    >
      {row.getVisibleCells().map((cell) => (
        <TableCell key={cell.id}>
          {flexRender(cell.column.columnDef.cell, cell.getContext())}
        </TableCell>
      ))}
    </TableRow>
  ));
}

const DEFAULT_PAGE_SIZES = [5, 10, 20, 50];

export function DataTable<TData, TValue>({
  columns,
  data,
  pageCount,
  pagination,
  onPaginationChange,
  sorting,
  onSortingChange,
  columnFilters,
  onColumnFiltersChange,
  isLoading,
  onRowClick,
  pageSizeOptions = DEFAULT_PAGE_SIZES,
}: DataTableProps<TData, TValue>) {
  "use no memo";
  const isServerSide = pageCount !== undefined;
  const effectivePagination = pagination ?? {
    pageIndex: 0,
    pageSize: pageSizeOptions[0],
  };

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    manualPagination: isServerSide,
    pageCount: isServerSide ? pageCount : undefined,
    manualSorting: isServerSide,
    manualFiltering: isServerSide,
    state: {
      pagination: effectivePagination,
      ...(sorting !== undefined && { sorting }),
      ...(columnFilters !== undefined && { columnFilters }),
    },
    onPaginationChange,
    onSortingChange,
    onColumnFiltersChange,
  });

  return (
    <div className="space-y-4">
      <div className="overflow-hidden rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow
                key={headerGroup.id}
                className="bg-muted/50 hover:bg-muted/50"
              >
                {headerGroup.headers.map((header) => {
                  const canSort = header.column.getCanSort();
                  const sorted = header.column.getIsSorted();
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder ? null : (
                        <div className="flex flex-col gap-1 py-1">
                          {canSort ? (
                            <button
                              type="button"
                              className="flex items-center gap-1 cursor-pointer select-none w-fit bg-transparent border-0 p-0 font-inherit"
                              onClick={header.column.getToggleSortingHandler()}
                              onKeyDown={(e) => {
                                if (e.key === "Enter" || e.key === " ") {
                                  e.preventDefault();
                                  header.column.getToggleSortingHandler()?.(e);
                                }
                              }}
                            >
                              {flexRender(
                                header.column.columnDef.header,
                                header.getContext(),
                              )}
                              <SortIcon sorted={sorted} />
                            </button>
                          ) : (
                            flexRender(
                              header.column.columnDef.header,
                              header.getContext(),
                            )
                          )}
                        </div>
                      )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              Array.from({ length: effectivePagination.pageSize }).map(
                (_, rowIndex) => (
                  <TableRow key={`skeleton-${String(rowIndex)}`}>
                    {columns.map((_, colIndex) => (
                      <TableCell
                        key={`skeleton-${String(rowIndex)}-${String(colIndex)}`}
                      >
                        <Skeleton
                          className="h-4 rounded"
                          style={{
                            width: `${60 + ((rowIndex * 3 + colIndex * 7) % 35)}%`,
                          }}
                        />
                      </TableCell>
                    ))}
                  </TableRow>
                ),
              )
            ) : (
              <TableBodyContent
                table={table}
                columns={columns}
                onRowClick={onRowClick}
              />
            )}
          </TableBody>
        </Table>
      </div>

      {(isServerSide ? pagination : effectivePagination) && (
        <div className="flex items-center justify-between px-1">
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <span>Rows per page</span>
            <Select
              value={String(effectivePagination.pageSize)}
              onValueChange={(v) =>
                onPaginationChange?.((prev) => ({
                  ...(prev as object),
                  pageIndex: 0,
                  pageSize: Number(v),
                }))
              }
            >
              <SelectTrigger size="sm" className="w-16 h-8">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {pageSizeOptions.map((size) => (
                  <SelectItem key={size} value={String(size)}>
                    {size}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex items-center gap-3">
            <p className="text-sm text-muted-foreground">
              Page {effectivePagination.pageIndex + 1} of{" "}
              {Math.max(
                isServerSide ? (pageCount ?? 1) : table.getPageCount(),
                1,
              )}
            </p>
            <div className="flex items-center gap-2">
              <Button
                variant="outline"
                size="sm"
                onClick={() => table.previousPage()}
                disabled={!table.getCanPreviousPage() || isLoading}
              >
                <ChevronLeft className="h-4 w-4 mr-1" />
                Previous
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => table.nextPage()}
                disabled={!table.getCanNextPage() || isLoading}
              >
                Next
                <ChevronRight className="h-4 w-4 ml-1" />
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
