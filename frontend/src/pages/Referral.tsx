import {
  useGetReferralsPaginated,
  useGetAllReferralStatuses,
  useUpdateReferralStatus,
  useSearchReferralStatuses,
} from "@/api/queries/useReferral";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
} from "@/components/Custom/Datatable/filter-builder";
import AsyncCombobox from "@/components/Custom/AsyncCombobox";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { Check, ExternalLink } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useForm, type FieldValues } from "react-hook-form";

type ReferralStatusOption = {
  pkReferralStatusId: string;
  referralStatusName: string;
};

type ReferralItem = {
  pkReferralId: string;
  name: string;
  email: string;
  cv?: { accessUrl?: string | null };
  job?: { title?: string };
  referralStatus?: string;
};

const referralFilterableColumns: FilterableColumn[] = [
  { id: "name", label: "Name", placeholder: "Search name..." },
  { id: "email", label: "Email", placeholder: "Search email..." },
  { id: "jobTitle", label: "Job", placeholder: "Search job..." },
  { id: "statusName", label: "Status", placeholder: "e.g. Pending" },
];

interface StatusUpdateCellProps {
  readonly referral: ReferralItem;
}

function StatusUpdateCell({ referral }: StatusUpdateCellProps) {
  const { mutate: updateReferralStatus, isPending } = useUpdateReferralStatus();
  const form = useForm<FieldValues>({
    defaultValues: { status: null },
  });
  const selectedStatus = form.watch("status") as ReferralStatusOption | null;

  return (
    <div className="flex items-center gap-2">
      <div className="w-50">
        <AsyncCombobox
          form={form}
          name="status"
          label=""
          placeholder="Change status..."
          fetchFunction={useSearchReferralStatuses}
          displayKey="referralStatusName"
          primaryKey="pkReferralStatusId"
          ref={null}
        />
      </div>

      <Button
        variant="ghost"
        size="icon"
        disabled={
          isPending ||
          !selectedStatus ||
          selectedStatus.pkReferralStatusId === referral.referralStatus
        }
        onClick={() =>
          updateReferralStatus({
            id: referral.pkReferralId,
            status: selectedStatus!.pkReferralStatusId,
          })
        }
      >
        <Check className="h-4 w-4 text-green-500" />
      </Button>
    </div>
  );
}

export default function Referral() {
  const { isLoading: statusLoading } = useGetAllReferralStatuses();

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
      name: filterText("name") || undefined,
      email: filterText("email") || undefined,
      "job-title": filterText("jobTitle") || undefined,
      status: filterText("statusName") || undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { data, isLoading } = useGetReferralsPaginated(params);
  const referrals: ReferralItem[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;

  const columns: ColumnDef<ReferralItem>[] = [
    {
      id: "sr",
      header: "Sr.",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) =>
        pagination.pageIndex * pagination.pageSize + row.index + 1,
    },
    {
      id: "name",
      accessorKey: "name",
      header: "Name",
      enableSorting: true,
      enableColumnFilter: true,
    },
    {
      id: "email",
      accessorKey: "email",
      header: "Email",
      enableSorting: true,
      enableColumnFilter: true,
    },
    {
      id: "cv",
      header: "CV",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => {
        const url = row.original.cv?.accessUrl;
        if (!url) return "-";
        return (
          <a
            href={url}
            target="_blank"
            rel="noopener noreferrer"
            onClick={(e) => e.stopPropagation()}
            className="inline-flex items-center justify-center rounded-md p-1 text-muted-foreground hover:text-foreground hover:bg-accent transition-colors"
            title={new URL(url).pathname.split("/").pop() ?? "CV"}
          >
            <ExternalLink className="h-4 w-4" />
          </a>
        );
      },
    },
    {
      id: "jobTitle",
      header: "Job",
      enableSorting: true,
      enableColumnFilter: true,
      accessorFn: (row) => row.job?.title ?? "",
      cell: ({ row }) => row.original.job?.title ?? "-",
    },
    {
      id: "statusName",
      header: "Status",
      enableSorting: true,
      enableColumnFilter: true,
      accessorFn: (row) => row.referralStatus ?? "",
      cell: ({ row }) => {
        const name = row.original.referralStatus;
        if (!name) return "-";
        return <Badge variant="secondary">{name}</Badge>;
      },
    },
    {
      id: "updateStatus",
      header: "Update Status",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => (
        <Can authority="MANAGE_ALL_REFERRAL">
          <StatusUpdateCell referral={row.original} />
        </Can>
      ),
    },
  ];

  return (
    <div className="p-8">
      <div className="mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Referrals
        </h4>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={referralFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={referrals}
          pageCount={pageCount}
          pagination={pagination}
          onPaginationChange={setPagination}
          sorting={sorting}
          onSortingChange={setSorting}
          columnFilters={columnFilters}
          onColumnFiltersChange={handleColumnFiltersChange}
          isLoading={isLoading || statusLoading}
        />
      </div>
    </div>
  );
}
