import axiosClient from "./axiosClient";

const expenseService = {
  addExpense: (expenseData) => axiosClient.post("/expenses/", expenseData),
  getExpenseTypeByName: (name: string) =>
    axiosClient.get("/expense-types/search", { params: { name } }),
  getUserTravelExpenses: (userTravelId: string) =>
    axiosClient.get(`travels/users/${userTravelId}/expenses`),
  approveExpense: (id) => axiosClient.patch(`/expenses/${id}/approve`),
  rejectExpense: (id) => axiosClient.patch(`/expenses/${id}/reject`),
  deleteExpense: (id) => axiosClient.delete(`/expense/${id}`),
};

export default expenseService;
