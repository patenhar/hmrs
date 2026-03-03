import { useGetUsersPaginated, useUpdateUserRole } from "@/api/queries/useUser";
import { useSearchRoles } from "@/api/queries/useRole";
import AsyncCombobox from "@/components/Custom/AsyncCombobox";
import Can from "@/components/Custom/Can";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
} from "@/components/Custom/Datatable/filter-builder";
import { Button } from "@/components/ui/button";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { Check } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useForm, type FieldValues } from "react-hook-form";

type UserRole = {
  pkRoleId: string;
  roleName: string;
};

type UserItem = {
  pkUserId: string;
  email: string;
  role?: UserRole;
  profile?: {
    name?: string;
  };
};

const userFilterableColumns: FilterableColumn[] = [
  { id: "email", label: "Email", placeholder: "Search email..." },
  { id: "name", label: "Name", placeholder: "Search name..." },
  { id: "roleName", label: "Role", placeholder: "e.g. Admin" },
];

interface RoleUpdateCellProps {
  readonly user: UserItem;
}

function RoleUpdateCell({ user }: RoleUpdateCellProps) {
  const { mutate: updateUserRole, isPending } = useUpdateUserRole();
  const form = useForm<FieldValues>({
    defaultValues: { role: null },
  });
  const selectedRole = form.watch("role") as UserRole | null;

  return (
    <div className="flex items-center gap-2">
      <AsyncCombobox
        form={form}
        name="role"
        label=""
        placeholder="Change role..."
        fetchFunction={useSearchRoles}
        displayKey="roleName"
        primaryKey="pkRoleId"
        ref={null}
      />
      <Button
        variant="outline"
        size="icon"
        disabled={
          isPending ||
          !selectedRole ||
          selectedRole.pkRoleId === user.role?.pkRoleId
        }
        onClick={() =>
          updateUserRole({
            userId: user.pkUserId,
            roleId: selectedRole!.pkRoleId,
          })
        }
      >
        <Check className="h-4 w-4 text-green-500" />
      </Button>
    </div>
  );
}

export default function Users() {
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
      email: filterText("email") || undefined,
      name: filterText("name") || undefined,
      "role-name": filterText("roleName") || undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { data, isLoading } = useGetUsersPaginated(params);
  const users: UserItem[] = data?.data?.content ?? [];
  const pageCount: number = data?.data?.totalPages ?? 0;

  const columns: ColumnDef<UserItem>[] = [
    {
      id: "sr",
      header: "Sr.",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) =>
        pagination.pageIndex * pagination.pageSize + row.index + 1,
    },
    {
      id: "email",
      accessorKey: "email",
      header: "Email",
      enableSorting: true,
      enableColumnFilter: true,
    },
    {
      id: "name",
      header: "Name",
      enableSorting: false,
      enableColumnFilter: true,
      accessorFn: (row) => row.profile?.name ?? "",
      cell: ({ row }) => row.original.profile?.name ?? "-",
    },
    {
      id: "currentRole",
      header: "Current Role",
      enableSorting: false,
      enableColumnFilter: true,
      accessorFn: (row) => row.role?.roleName ?? "",
      cell: ({ row }) => row.original.role?.roleName ?? "-",
    },
    {
      id: "updateRole",
      header: "Update Role",
      enableSorting: false,
      enableColumnFilter: false,
      cell: ({ row }) => (
        <Can authority="MANAGE_ALL_USER">
          <RoleUpdateCell user={row.original} />
        </Can>
      ),
    },
  ];

  return (
    <div className="p-8">
      <div className="mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Users
        </h4>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={userFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={users}
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
    </div>
  );
}
