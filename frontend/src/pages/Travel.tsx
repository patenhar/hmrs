import {
  useDeleteTravel,
  useGetTravelsPaginated,
} from "@/api/queries/useTravel.ts";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
  type RangeValue,
} from "@/components/Custom/Datatable/filter-builder";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

interface TravelResDto {
  pkTravelId: string;
  title: string;
  description: string;
  travelDate: string;
  returnDate: string;
  maxGrantPerDay: number;
  hrMail: string;
}

const travelFilterableColumns: FilterableColumn[] = [
  { id: "title", label: "Title", placeholder: "Search title…" },
  {
    id: "description",
    label: "Description",
    placeholder: "Search description…",
  },
  { id: "travelDate", label: "Travel Date", placeholder: "e.g. 2025-01-15" },
  { id: "returnDate", label: "Return Date", placeholder: "e.g. 2025-01-20" },
  { id: "maxGrantPerDay", label: "Grant Limit", type: "range" as const },
  { id: "hrMail", label: "HR Mail", placeholder: "Search HR mail…" },
];

const columns: ColumnDef<TravelResDto>[] = [
  {
    accessorKey: "title",
    header: "Title",
    enableSorting: true,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "Search title..." },
  },
  {
    accessorKey: "description",
    header: "Description",
    enableSorting: false,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "Search description..." },
  },
  {
    accessorKey: "travelDate",
    header: "Travel Date",
    enableSorting: true,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "e.g. 2025-01-15" },
  },
  {
    accessorKey: "returnDate",
    header: "Return Date",
    enableSorting: true,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "e.g. 2025-01-20" },
  },
  {
    accessorKey: "maxGrantPerDay",
    header: "Grant Limit",
    enableSorting: true,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "e.g. 100" },
  },
  {
    accessorKey: "hrMail",
    header: "HR Mail",
    enableSorting: true,
    enableColumnFilter: true,
    meta: { filterPlaceholder: "Search HR mail..." },
  },
  {
    id: "actions",
    header: "Actions",
    enableSorting: false,
    enableColumnFilter: false,
    cell: ({ row }) => <TravelActions travel={row.original} />,
  },
];

function TravelActions({ travel }: { readonly travel: TravelResDto }) {
  const { mutate: deleteTravel } = useDeleteTravel();
  const navigate = useNavigate();
  return (
    <Can authority="MANAGE_ALL_TRAVEL">
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant="ghost"
            size="icon"
            onClick={(e) => e.stopPropagation()}
            aria-label="Open actions"
          >
            <MoreHorizontal className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuItem
            onClick={(e) => {
              e.stopPropagation();
              navigate(`${travel.pkTravelId}/update`);
            }}
          >
            <Pencil className="h-4 w-4 mr-2" />
            Edit
          </DropdownMenuItem>
          <DropdownMenuItem
            variant="destructive"
            onClick={(e) => {
              e.stopPropagation();
              deleteTravel(travel.pkTravelId);
            }}
          >
            <Trash className="h-4 w-4 mr-2" />
            Delete
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </Can>
  );
}

export default function Travel() {
  const navigate = useNavigate();

  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 5,
  });
  const [sorting, setSorting] = useState<SortingState>([
    { id: "travelDate", desc: true },
  ]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [debouncedFilters, setDebouncedFilters] =
    useState<ColumnFiltersState>(columnFilters);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedFilters(columnFilters), 500);
    return () => clearTimeout(timer);
  }, [columnFilters]);

  const handleColumnFiltersChange = (
    updater:
      | ColumnFiltersState
      | ((prev: ColumnFiltersState) => ColumnFiltersState),
  ) => {
    setColumnFilters(updater);
    setPagination((prev) => ({ ...prev, pageIndex: 0 }));
  };

  const filterText = (id: string) =>
    debouncedFilters.find((f) => f.id === id)?.value as string | undefined;

  const filterRange = (id: string): RangeValue | undefined => {
    const v = debouncedFilters.find((f) => f.id === id)?.value;
    if (v && typeof v === "object") return v as RangeValue;
    return undefined;
  };

  const grantRange = filterRange("maxGrantPerDay");

  const sortParam = useMemo(
    () =>
      JSON.stringify(
        sorting.map((s) => ({
          field: s.id,
          direction: s.desc ? "desc" : "asc",
        })),
      ),
    [sorting],
  );

  const params = useMemo(
    () => ({
      page: pagination.pageIndex,
      size: pagination.pageSize,
      sort: sortParam,
      title: filterText("title"),
      description: filterText("description"),
      "travel-date": filterText("travelDate"),
      "return-date": filterText("returnDate"),
      "grant-limit-min": grantRange?.min || undefined,
      "grant-limit-max": grantRange?.max || undefined,
      "hr-mail": filterText("hrMail"),
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { data, isLoading } = useGetTravelsPaginated(params);
  const travels: TravelResDto[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Travels
        </h4>
        <Can authority={"MANAGE_ALL_TRAVEL"}>
          <Button onClick={() => navigate("add")}>Add Travel</Button>
        </Can>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={travelFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={travels}
          pageCount={pageCount}
          pagination={pagination}
          onPaginationChange={setPagination}
          sorting={sorting}
          onSortingChange={setSorting}
          columnFilters={columnFilters}
          onColumnFiltersChange={handleColumnFiltersChange}
          isLoading={isLoading}
          onRowClick={(travel) => navigate(travel.pkTravelId)}
        />
      </div>
    </div>
  );
}
