import {
  useAddPermission,
  useGetAllPermissions,
} from "@/api/queries/usePermission";
import {
  useAddRole,
  useGetRolesPaginated,
  useUpdateRole,
} from "@/api/queries/useRole";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { DataTable } from "@/components/Custom/Datatable/data-table";
import {
  FilterBuilder,
  type FilterableColumn,
} from "@/components/Custom/Datatable/filter-builder";
import type {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
} from "@tanstack/react-table";
import { useEffect, useMemo, useState } from "react";

type PermissionItem = {
  pkPermissionId: string;
  permissionName: string;
};

type RoleItem = {
  pkRoleId: string;
  roleName: string;
  permissions?: PermissionItem[];
};

const roleFilterableColumns: FilterableColumn[] = [
  { id: "roleName", label: "Role", placeholder: "Search roleâ€¦" },
];

export default function RolesPermissions() {
  const { data: permissionData, isLoading: permissionsLoading } =
    useGetAllPermissions();
  const { mutate: addRole, isPending: addRolePending } = useAddRole();
  const { mutate: updateRole, isPending: updateRolePending } = useUpdateRole();
  const { mutate: addPermission, isPending: addPermissionPending } =
    useAddPermission();

  const [permissionName, setPermissionName] = useState("");
  const [roleName, setRoleName] = useState("");
  const [newRolePermissionIds, setNewRolePermissionIds] = useState<string[]>(
    [],
  );
  const [addPermissionOpen, setAddPermissionOpen] = useState(false);
  const [addRoleOpen, setAddRoleOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [editingRole, setEditingRole] = useState<RoleItem | null>(null);
  const [editPermissionIds, setEditPermissionIds] = useState<string[]>([]);

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
      "role-name": filterText("roleName") || undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pagination, sortParam, debouncedFilters],
  );

  const { data: roleData, isLoading: rolesLoading } =
    useGetRolesPaginated(params);
  const roles: RoleItem[] = roleData?.data?.content ?? [];
  const pageCount: number = roleData?.data?.totalPages ?? 0;

  const permissions: PermissionItem[] = permissionData?.data?.data ?? [];

  const columns: ColumnDef<RoleItem>[] = useMemo(
    () => [
      {
        id: "sr",
        header: "Sr.",
        enableSorting: false,
        enableColumnFilter: false,
        cell: ({ row }) => row.index + 1,
      },
      {
        id: "roleName",
        accessorKey: "roleName",
        header: "Role",
        enableSorting: true,
        enableColumnFilter: true,
      },
      {
        id: "actions",
        header: "",
        enableSorting: false,
        enableColumnFilter: false,
        cell: ({ row }) => (
          <Button variant="outline" onClick={() => openEditRole(row.original)}>
            Assign Permissions
          </Button>
        ),
      },
    ],
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [],
  );

  const toggleNewRolePermission = (permissionId: string) => {
    setNewRolePermissionIds((prev) =>
      prev.includes(permissionId)
        ? prev.filter((id) => id !== permissionId)
        : [...prev, permissionId],
    );
  };

  const toggleEditPermission = (permissionId: string) => {
    setEditPermissionIds((prev) =>
      prev.includes(permissionId)
        ? prev.filter((id) => id !== permissionId)
        : [...prev, permissionId],
    );
  };

  const onAddPermission = () => {
    const trimmed = permissionName.trim();
    if (!trimmed) return;
    addPermission(
      { permissionName: trimmed },
      {
        onSuccess: () => {
          setPermissionName("");
          setAddPermissionOpen(false);
        },
      },
    );
  };

  const onAddRole = () => {
    const trimmed = roleName.trim();
    if (!trimmed || newRolePermissionIds.length === 0) return;
    addRole(
      {
        roleName: trimmed,
        permissionIds: newRolePermissionIds,
      },
      {
        onSuccess: () => {
          setRoleName("");
          setNewRolePermissionIds([]);
          setAddRoleOpen(false);
        },
      },
    );
  };

  const openEditRole = (role: RoleItem) => {
    setEditingRole(role);
    setEditPermissionIds(role.permissions?.map((p) => p.pkPermissionId) ?? []);
    setEditOpen(true);
  };

  const onSaveRolePermissions = () => {
    if (!editingRole) return;
    updateRole(
      {
        roleId: editingRole.pkRoleId,
        data: {
          roleName: editingRole.roleName,
          permissionIds: editPermissionIds,
        },
      },
      {
        onSuccess: () => {
          setEditOpen(false);
          setEditingRole(null);
          setEditPermissionIds([]);
        },
      },
    );
  };

  return (
    <div className="p-8 space-y-6">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Roles & Permissions
        </h4>
        <div className="flex items-center gap-3">
          <Button onClick={() => setAddPermissionOpen(true)}>
            Add Permission
          </Button>
          <Button onClick={() => setAddRoleOpen(true)}>Add Role</Button>
        </div>
      </div>

      <div className="space-y-3">
        <FilterBuilder
          filterableColumns={roleFilterableColumns}
          columnFilters={columnFilters}
          onFiltersChange={handleColumnFiltersChange}
        />

        <DataTable
          columns={columns}
          data={roles}
          pageCount={pageCount}
          pagination={pagination}
          onPaginationChange={setPagination}
          sorting={sorting}
          onSortingChange={setSorting}
          columnFilters={columnFilters}
          onColumnFiltersChange={handleColumnFiltersChange}
          isLoading={rolesLoading || permissionsLoading}
        />
      </div>

      <Dialog open={addPermissionOpen} onOpenChange={setAddPermissionOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>Add Permission</DialogTitle>
          </DialogHeader>

          <Input
            placeholder="Permission name"
            value={permissionName}
            onChange={(e) => setPermissionName(e.target.value)}
          />

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => setAddPermissionOpen(false)}
            >
              Cancel
            </Button>
            <Button disabled={addPermissionPending} onClick={onAddPermission}>
              Add
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={addRoleOpen} onOpenChange={setAddRoleOpen}>
        <DialogContent className="sm:max-w-2xl">
          <DialogHeader>
            <DialogTitle>Add Role</DialogTitle>
          </DialogHeader>

          <Input
            className="max-w-lg"
            placeholder="Role name"
            value={roleName}
            onChange={(e) => setRoleName(e.target.value)}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-2 max-h-52 overflow-auto border rounded-md p-3">
            {permissions.map((permission) => (
              <label
                key={permission.pkPermissionId}
                className="flex items-center gap-2 text-sm"
              >
                <Checkbox
                  checked={newRolePermissionIds.includes(
                    permission.pkPermissionId,
                  )}
                  onCheckedChange={() =>
                    toggleNewRolePermission(permission.pkPermissionId)
                  }
                />
                <span>{permission.permissionName}</span>
              </label>
            ))}
          </div>

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setAddRoleOpen(false);
                setRoleName("");
                setNewRolePermissionIds([]);
              }}
            >
              Cancel
            </Button>
            <Button disabled={addRolePending} onClick={onAddRole}>
              Add Role
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={editOpen} onOpenChange={setEditOpen}>
        <DialogContent className="sm:max-w-2xl">
          <DialogHeader>
            <DialogTitle>Assign Permissions</DialogTitle>
            <DialogDescription>
              {editingRole?.roleName ? `Role: ${editingRole.roleName}` : ""}
            </DialogDescription>
          </DialogHeader>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-2 max-h-80 overflow-auto border rounded-md p-3">
            {permissions.map((permission) => (
              <label
                key={permission.pkPermissionId}
                className="flex items-center gap-2 text-sm"
              >
                <Checkbox
                  checked={editPermissionIds.includes(
                    permission.pkPermissionId,
                  )}
                  onCheckedChange={() =>
                    toggleEditPermission(permission.pkPermissionId)
                  }
                />
                <span>{permission.permissionName}</span>
              </label>
            ))}
          </div>

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setEditOpen(false);
                setEditingRole(null);
                setEditPermissionIds([]);
              }}
            >
              Cancel
            </Button>
            <Button
              disabled={updateRolePending}
              onClick={onSaveRolePermissions}
            >
              Save
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
