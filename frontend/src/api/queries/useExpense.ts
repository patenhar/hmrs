import {
  useMutation,
  useQuery,
  useQueryClient,
  keepPreviousData,
} from "@tanstack/react-query";
import { useMemo } from "react";
import expenseService from "../expenseService.tsx";
import type { ExpensePageParams } from "../expenseService.tsx";
import { toast } from "sonner";

const {
  addExpense,
  getUserTravelExpenses,
  getExpensesPaginated,
  approveExpense,
  rejectExpense,
  deleteExpense,
} = expenseService;

const EXPENSE_TYPES = [
  { pkExpenseTypeId: "ACCOMMODATION", expenseTypeName: "Accommodation" },
  { pkExpenseTypeId: "FOOD_AND_BEVERAGE", expenseTypeName: "Food & Beverage" },
  { pkExpenseTypeId: "TRANSPORTATION", expenseTypeName: "Transportation" },
  { pkExpenseTypeId: "COMMUNICATION", expenseTypeName: "Communication" },
  { pkExpenseTypeId: "ENTERTAINMENT", expenseTypeName: "Entertainment" },
  { pkExpenseTypeId: "OTHER", expenseTypeName: "Other" },
];

export const useGetExpenseType = (search: string) => {
  const q = (search ?? "").trim().toLowerCase();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const filtered = useMemo(
    () =>
      q
        ? EXPENSE_TYPES.filter((t) =>
            t.expenseTypeName.toLowerCase().includes(q),
          )
        : EXPENSE_TYPES,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [q],
  );
  return { data: { data: { data: filtered } }, isLoading: false, error: null };
};

export const useAddExpense = (userTravelId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: addExpense,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Expense", userTravelId] });
    },
    onError: (
      error: Error & { errors?: { field: string; message: string }[] },
    ) => {
      const fieldErrors = error.errors;
      if (fieldErrors && fieldErrors.length > 0) {
        toast.error(error.message || "Expense creation failed", {
          description: fieldErrors
            .map((e) => `${e.field}: ${e.message}`)
            .join("\n"),
        });
      } else {
        toast.error("Expense creation failed", {
          description: error.message || "Something went wrong",
        });
      }
    },
  });
};

export const useGetUserTravelExpenses = (id: string) => {
  return useQuery({
    queryKey: ["Expense", id],
    queryFn: () => getUserTravelExpenses(id),
  });
};

export const useGetExpensesPaginated = (
  userTravelId: string,
  params: ExpensePageParams,
) => {
  return useQuery({
    queryKey: ["Expense", userTravelId, "paginated", params],
    queryFn: () => getExpensesPaginated(userTravelId, params),
    placeholderData: keepPreviousData,
  });
};

export const useApproveExpense = (userTravelId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: approveExpense,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Expense", userTravelId] });
    },
    onError: (error) => {
      toast.error("Expense creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useRejectExpense = (userTravelId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, remark }: { id: number; remark: string }) =>
      rejectExpense({ id, remark }),
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Expense", userTravelId] });
    },
    onError: (error) => {
      toast.error("Expense creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useDeleteExpense = (userTravelId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteExpense,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Expense", userTravelId] });
    },
    onError: (error) => {
      toast.error("Expense deletion failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
