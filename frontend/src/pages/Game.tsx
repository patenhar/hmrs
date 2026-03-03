import { useGetGamesPaginated } from "@/api/queries/useGames";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
  type RangeValue,
} from "@/components/Custom/Datatable/filter-builder";
import { Button } from "@/components/ui/button";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { useEffect, useMemo, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";

type GameItem = {
  pkGameId: string;
  gameName: string;
  maxPlayers: number;
  duration: number;
  operationHourBegin: string;
  operationHourEnd: string;
  bookingCycle: number;
};

const gameFilterableColumns: FilterableColumn[] = [
  { id: "gameName", label: "Game", placeholder: "Search game…" },
  { id: "maxPlayers", label: "Max Players", type: "range" as const },
];

const columns: ColumnDef<GameItem>[] = [
  {
    id: "sr",
    header: "Sr.",
    enableSorting: false,
    enableColumnFilter: false,
    cell: ({ row }) => row.index + 1,
  },
  {
    id: "gameName",
    accessorKey: "gameName",
    header: "Game",
    enableSorting: true,
    enableColumnFilter: true,
  },
  {
    id: "maxPlayers",
    accessorKey: "maxPlayers",
    header: "Player limit",
    enableSorting: true,
    enableColumnFilter: true,
  },
  {
    id: "duration",
    accessorKey: "duration",
    header: "Duration",
    enableSorting: true,
    enableColumnFilter: false,
  },
  {
    id: "operationHours",
    header: "Operation hours",
    enableSorting: false,
    enableColumnFilter: false,
    accessorFn: (row) => `${row.operationHourBegin} - ${row.operationHourEnd}`,
  },
  {
    id: "bookingCycle",
    accessorKey: "bookingCycle",
    header: "Booking cycle",
    enableSorting: true,
    enableColumnFilter: false,
  },
];

export default function Game() {
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

  const maxPlayersRange = filterRange("maxPlayers");

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
      "game-name": filterText("gameName") || undefined,
      "max-players-min": maxPlayersRange?.min
        ? Number(maxPlayersRange.min)
        : undefined,
      "max-players-max": maxPlayersRange?.max
        ? Number(maxPlayersRange.max)
        : undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { data, isLoading } = useGetGamesPaginated(params);
  const games: GameItem[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Games
        </h4>
        <div className="flex gap-2">
          <Can authority={"MANAGE_ALL_GAME"}>
            <Button onClick={() => navigate("/games/add")}>Add Game</Button>
          </Can>
        </div>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={gameFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={games}
          pageCount={pageCount}
          pagination={pagination}
          onPaginationChange={setPagination}
          sorting={sorting}
          onSortingChange={setSorting}
          columnFilters={columnFilters}
          onColumnFiltersChange={handleColumnFiltersChange}
          isLoading={isLoading}
          onRowClick={(row) => navigate(`/games/${row.pkGameId}/update`)}
        />
      </div>

      <Outlet />
    </div>
  );
}
