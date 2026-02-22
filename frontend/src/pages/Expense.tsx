import {
  useApproveExpense,
  useGetUserTravelExpenses,
  useRejectExpense,
} from "@/api/queries/useExpense";
import { useDeleteTravelDocument } from "@/api/queries/useTravel";
import ButtonLink from "@/components/Custom/ButtonLink";
import { NoDocument } from "@/components/Custom/NoDocument";
import { NoExpense } from "@/components/Custom/NoExpense";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Check, Cross, Trash, X } from "lucide-react";
import { Outlet, useNavigate, useParams } from "react-router-dom";

export default function Expense() {
  const { userTravelId } = useParams();
  const { isLoading, data } = useGetUserTravelExpenses(userTravelId);
  const { mutate: approveExpense } = useApproveExpense(userTravelId);
  const { mutate: rejectExpense } = useRejectExpense(userTravelId);

  // const { isPending, mutate: deleteData } =
  //   useDeleteTravelDocument(userTravelId);
  var expenses = [];
  if (!isLoading) {
    expenses = data?.data.data;
    console.log(expenses);
  }
  const navigate = useNavigate();
  return expenses.length == 0 ? (
    <NoExpense />
  ) : (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Expenses
        </h4>
        <Button onClick={() => navigate("add")}>Add Expense</Button>
      </div>
      <Table>
        <TableHeader className="bg-gray-50">
          <TableRow>
            <TableHead>Sr.</TableHead>
            <TableHead>Type</TableHead>
            <TableHead>Description</TableHead>
            <TableHead>Amount</TableHead>
            <TableHead>Document</TableHead>
            <TableHead>Status</TableHead>
            <TableHead></TableHead>
            <TableHead></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {expenses.map((e, idx) => (
            <TableRow key={e.pkExpenseId} className="cursor-pointer">
              <TableCell>{idx + 1}</TableCell>
              <TableCell>{e.expenseType.expenseTypeName}</TableCell>
              <TableCell>{e.description}</TableCell>
              <TableCell>{e.amount}</TableCell>
              <TableCell>
                <ButtonLink
                  to={e.document.accessUrl}
                  text={new URL(e.document.accessUrl).pathname.split("/").pop()}
                />
              </TableCell>
              <TableCell>{e.expenseStatus.expenseStatusName}</TableCell>
              <TableCell>
                <Button
                  variant="ghost"
                  size="icon"
                  className="p-0 flex justify-end"
                  onClick={() => {
                    approveExpense(e.pkExpenseId);
                  }}
                >
                  <Check className="h-4 w-4 text-green-500" />
                </Button>
              </TableCell>
              <TableCell>
                <Button
                  variant="ghost"
                  size="icon"
                  className="p-0 flex justify-end"
                  onClick={() => {
                    rejectExpense(e.pkExpenseId);
                  }}
                >
                  <X className="h-4 w-4 text-red-500" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Outlet />
    </div>
  );
}
