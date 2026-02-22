import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import expenseService from "../expenseService.tsx";
import { toast } from "sonner";

const {
  addExpense,
  getExpenseTypeByName,
  getUserTravelExpenses,
  approveExpense,
  rejectExpense,
} = expenseService;

export const useGetExpenseType = (name: string) => {
  return useQuery({
    queryKey: ["Department", name],
    queryFn: () => getExpenseTypeByName(name),
    enabled: name.length > 0,
  });
};

export const useAddExpense = (userTravelId: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: addExpense,
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

export const useGetUserTravelExpenses = (id: string) => {
  return useQuery({
    queryKey: ["Expense", id],
    queryFn: () => getUserTravelExpenses(id),
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
    mutationFn: rejectExpense,
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
