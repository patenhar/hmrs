import {
  useApproveExpense,
  useDeleteExpense,
  useGetExpensesPaginated,
} from "@/api/queries/useExpense";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
  type RangeValue,
} from "@/components/Custom/Datatable/filter-builder";
import { NoExpense } from "@/components/Custom/NoExpense";
import { RejectExpenseDialog } from "@/components/Custom/RejectExpenseDialog";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { Check, ExternalLink, MoreHorizontal, Trash, X } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { Outlet, useNavigate, useParams } from "react-router-dom";

interface ExpenseResDto {
  pkExpenseId: number;
  amount: number;
  description: string;
  document: { accessUrl: string };
  expenseType: string;
  expenseStatus: string;
  lastActionBy: { email: string } | null;
  remarks: string | null;
}

const expenseFilterableColumns: FilterableColumn[] = [
  { id: "expenseTypeName", label: "Type", placeholder: "Search type..." },
  { id: "description", label: "Description", placeholder: "Search..." },
  { id: "amount", label: "Amount", type: "range" },
  { id: "expenseStatusName", label: "Status", placeholder: "e.g. Approved" },
  { id: "actorEmail", label: "Actor", placeholder: "Search email..." },
];

interface ExpenseActionsProps {
  readonly expense: ExpenseResDto;
  readonly userTravelId: string;
  readonly onReject: (id: number) => void;
}

function ExpenseActions({
  expense,
  userTravelId,
  onReject,
}: ExpenseActionsProps) {
  const { mutate: approveExpense } = useApproveExpense(userTravelId);
  const { mutate: deleteExpense } = useDeleteExpense(userTravelId);
  const isApproved = expense.expenseStatus?.toLowerCase() === "approved";

  return (
    <Can authority="MANAGE_EXPENSE" extraAuthority="MANAGE_ALL_EXPENSE">
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
              approveExpense(expense.pkExpenseId);
            }}
          >
            <Check className="h-4 w-4 mr-2 text-green-500" />
            Approve
          </DropdownMenuItem>
          <DropdownMenuItem
            onClick={(e) => {
              e.stopPropagation();
              onReject(expense.pkExpenseId);
            }}
          >
            <X className="h-4 w-4 mr-2 text-orange-500" />
            Reject
          </DropdownMenuItem>
          {!isApproved && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                variant="destructive"
                onClick={(e) => {
                  e.stopPropagation();
                  deleteExpense(expense.pkExpenseId);
                }}
              >
                <Trash className="h-4 w-4 mr-2" />
                Delete
              </DropdownMenuItem>
            </>
          )}
        </DropdownMenuContent>
      </DropdownMenu>
    </Can>
  );
}

export default function Expense() {
  const { userTravelId } = useParams<{ userTravelId: string }>();
  const navigate = useNavigate();

  const [rejectDialogOpen, setRejectDialogOpen] = useState(false);
  const [selectedExpenseId, setSelectedExpenseId] = useState<number | null>(
    null,
  );
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

  const amountRange = filterRange("amount");

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
      description: filterText("description") || undefined,
      type: filterText("expenseTypeName") || undefined,
      status: filterText("expenseStatusName") || undefined,
      actor: filterText("actorEmail") || undefined,
      "amount-min": amountRange?.min || undefined,
      "amount-max": amountRange?.max || undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { isLoading, data } = useGetExpensesPaginated(userTravelId!, params);
  const expenses: ExpenseResDto[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;
  const totalElements: number = data?.data?.totalElements ?? -1;

  const approvedTotal = expenses
    .filter((e) => e.expenseStatus?.toLowerCase() === "approved")
    .reduce((sum, e) => sum + (Number(e.amount) || 0), 0);

  const columns: ColumnDef<ExpenseResDto>[] = [
    {
      id: "sr",
      header: "Sr.",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) =>
        pagination.pageIndex * pagination.pageSize + row.index + 1,
    },
    {
      id: "expenseTypeName",
      header: "Type",
      enableSorting: true,
      enableColumnFilter: true,
      accessorFn: (row) => row.expenseType ?? "",
    },
    {
      accessorKey: "description",
      header: "Description",
      enableSorting: true,
      enableColumnFilter: true,
    },
    {
      accessorKey: "amount",
      header: "Amount",
      enableSorting: true,
      enableColumnFilter: true,
    },
    {
      id: "document",
      header: "Document",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => {
        const url = row.original.document?.accessUrl;
        if (!url) return "-";
        return (
          <a
            href={url}
            target="_blank"
            rel="noopener noreferrer"
            onClick={(e) => e.stopPropagation()}
            className="inline-flex items-center justify-center rounded-md p-1 text-muted-foreground hover:text-foreground hover:bg-accent transition-colors"
            title={new URL(url).pathname.split("/").pop() ?? "file"}
          >
            <ExternalLink className="h-4 w-4" />
          </a>
        );
      },
    },
    {
      id: "expenseStatusName",
      header: "Status",
      enableSorting: true,
      enableColumnFilter: true,
      accessorFn: (row) => row.expenseStatus ?? "",
      cell: ({ row }) => {
        const status = row.original.expenseStatus?.toLowerCase();
        const label = row.original.expenseStatus ?? "-";
        if (status === "approved") {
          return (
            <Badge className="bg-green-500/10 text-green-600 dark:bg-green-500/20 dark:text-green-400">
              {label}
            </Badge>
          );
        }
        if (status === "rejected") {
          return <Badge variant="destructive">{label}</Badge>;
        }
        return <Badge variant="secondary">{label}</Badge>;
      },
    },
    {
      id: "actorEmail",
      header: "Actor",
      enableSorting: true,
      enableColumnFilter: true,
      accessorFn: (row) => row.lastActionBy?.email ?? "",
      cell: ({ row }) => row.original.lastActionBy?.email ?? "-",
    },
    {
      accessorKey: "remarks",
      header: "Remarks",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => row.original.remarks ?? "-",
    },
    {
      id: "actions",
      header: "Actions",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => (
        <ExpenseActions
          expense={row.original}
          userTravelId={userTravelId!}
          onReject={(id) => {
            setSelectedExpenseId(id);
            setRejectDialogOpen(true);
          }}
        />
      ),
    },
  ];

  const hasActiveFilters = debouncedFilters.length > 0 || sorting.length > 0;

  if (!isLoading && totalElements === 0 && !hasActiveFilters) {
    return <NoExpense />;
  }

  return (
    <div className="p-8">
      <RejectExpenseDialog
        expenseId={selectedExpenseId}
        open={rejectDialogOpen}
        onOpenChange={setRejectDialogOpen}
      />

      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-3">
          <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
            Expenses
          </h4>
          <Badge variant="secondary" className="text-sm font-medium">
            Approved: ₹{approvedTotal.toLocaleString()}
          </Badge>
        </div>
        <Button onClick={() => navigate("add")}>Add Expense</Button>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={expenseFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={setColumnFilters}
        />

        <DataTable
          columns={columns}
          data={expenses}
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
