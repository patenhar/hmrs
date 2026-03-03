import axiosClient from "./axiosClient";

export interface ExpensePageParams {
  page: number;
  size: number;
  sort: string;
  description?: string;
  type?: string;
  status?: string;
  actor?: string;
  "amount-min"?: string;
  "amount-max"?: string;
}

const expenseService = {
  addExpense: (expenseData) => axiosClient.post("/expenses/", expenseData),
  getExpenseTypeByName: (name: string) =>
    axiosClient.get("/expense-types/search", { params: { name } }),
  getUserTravelExpenses: (userTravelId: string) =>
    axiosClient.get(`travels/users/${userTravelId}/expenses`),
  getExpensesPaginated: (userTravelId: string, params: ExpensePageParams) =>
    axiosClient.get(
      `travels/users/${userTravelId}/expenses/filtering&pagination&sorting`,
      { params },
    ),
  approveExpense: (id) => axiosClient.patch(`/expenses/${id}/approve`),
  rejectExpense: ({ id, remark }: { id: number; remark: string }) =>
    axiosClient.patch(`/expenses/${id}/reject`, remark, {
      headers: { "Content-Type": "text/plain" },
    }),
  deleteExpense: (id) => axiosClient.delete(`/expenses/${id}`),
};

export default expenseService;
