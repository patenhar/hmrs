import { useGetGameBookingsPaginated } from "@/api/queries/useGames";
import { useAuth } from "@/context/AuthContext";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
  type RangeValue,
} from "@/components/Custom/Datatable/filter-builder";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { useEffect, useMemo, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";

type GameBookingItem = {
  pkGameBookingId: string;
  gameSlot: {
    pkGameSlotId: string;
    date: string;
    beginTime: string;
    endTime: string;
    game: { pkGameId: string; gameName: string };
  };
  bookedBy: { pkUserId: string; email: string };
  gameBookingStatus: string;
  teamMembers: { pkUserId: string; email: string }[];
};

const bookingFilterableColumns: FilterableColumn[] = [
  { id: "gameName", label: "Game", placeholder: "Search game…" },
  { id: "statusName", label: "Status", placeholder: "Search status…" },
  { id: "date", label: "Date", type: "range" as const },
];

const columns: ColumnDef<GameBookingItem>[] = [
  {
    id: "sr",
    header: "Sr.",
    enableSorting: false,
    enableColumnFilter: false,
    cell: ({ row }) => row.index + 1,
  },
  {
    id: "gameName",
    header: "Game",
    enableSorting: true,
    enableColumnFilter: true,
    accessorFn: (row) => row.gameSlot.game.gameName,
  },
  {
    id: "date",
    header: "Date",
    enableSorting: true,
    enableColumnFilter: true,
    accessorFn: (row) => row.gameSlot.date,
  },
  {
    id: "beginTime",
    header: "Begin time",
    enableSorting: true,
    enableColumnFilter: false,
    accessorFn: (row) => row.gameSlot.beginTime,
  },
  {
    id: "endTime",
    header: "End time",
    enableSorting: true,
    enableColumnFilter: false,
    accessorFn: (row) => row.gameSlot.endTime,
  },
  {
    id: "bookedBy",
    header: "Booked by",
    enableSorting: true,
    enableColumnFilter: false,
    accessorFn: (row) => row.bookedBy.email,
  },
  {
    id: "status",
    header: "Status",
    enableSorting: true,
    enableColumnFilter: true,
    accessorFn: (row) => row.gameBookingStatus,
  },
  {
    id: "teamMembers",
    header: "Team",
    enableSorting: false,
    enableColumnFilter: false,
    cell: ({ row }) => {
      const members = row.original.teamMembers ?? [];
      if (members.length === 0)
        return <span className="text-muted-foreground text-xs">—</span>;
      return (
        <Tooltip>
          <TooltipTrigger asChild>
            <span className="cursor-default underline decoration-dotted text-sm">
              {members.length} member{members.length > 1 ? "s" : ""}
            </span>
          </TooltipTrigger>
          <TooltipContent>
            <ul className="my-1 ml-4 list-disc [&>li]:mt-1 text-xs">
              {members.map((m) => (
                <li key={m.pkUserId}>{m.email}</li>
              ))}
            </ul>
          </TooltipContent>
        </Tooltip>
      );
    },
  },
];

export default function GameBooking() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });
  const [sorting, setSorting] = useState<SortingState>([]);
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

  const dateRange = filterRange("date");

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
      "user-id": user?.pkUserId,
      "game-name": filterText("gameName") || undefined,
      "status-name": filterText("statusName") || undefined,
      "date-from": dateRange?.min ? String(dateRange.min) : undefined,
      "date-to": dateRange?.max ? String(dateRange.max) : undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters, user?.pkUserId],
  );

  const { data, isLoading } = useGetGameBookingsPaginated(params);
  const bookings: GameBookingItem[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Game Bookings
        </h4>
        <div className="flex gap-2">
          <Can authority={"ADD_BOOKING"} extraAuthority={"MANAGE_ALL_BOOKING"}>
            <Button onClick={() => navigate("add")}>Book Slot</Button>
          </Can>
        </div>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={bookingFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={bookings}
          pageCount={pageCount}
          pagination={pagination}
          onPaginationChange={setPagination}
          sorting={sorting}
          onSortingChange={setSorting}
          columnFilters={columnFilters}
          onColumnFiltersChange={handleColumnFiltersChange}
          isLoading={isLoading}
        />
      </div>

      <Outlet />
    </div>
  );
}
