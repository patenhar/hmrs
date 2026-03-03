import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useRejectExpense } from "@/api/queries/useExpense";
import { useParams } from "react-router-dom";

const formSchema = z.object({
  remark: z.string().min(1, "Remark is required"),
});

interface RejectExpenseDialogProps {
  expenseId: number | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function RejectExpenseDialog({
  expenseId,
  open,
  onOpenChange,
}: Readonly<RejectExpenseDialogProps>) {
  const { userTravelId } = useParams();
  const { mutate: rejectExpense, isPending } = useRejectExpense(userTravelId!);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      remark: "",
    },
  });

  function onSubmit(data: z.infer<typeof formSchema>) {
    if (!expenseId) return;
    rejectExpense(
      { id: expenseId, remark: data.remark },
      {
        onSuccess: () => {
          form.reset();
          onOpenChange(false);
        },
      },
    );
  }

  return (
    <Dialog
      open={open}
      onOpenChange={(o) => {
        if (!o) form.reset();
        onOpenChange(o);
      }}
    >
      <form id="reject-expense-form" onSubmit={form.handleSubmit(onSubmit)}>
        <DialogContent className="sm:max-w-sm">
          <DialogHeader>
            <DialogTitle>Reject Expense</DialogTitle>
            <DialogDescription>
              Please provide a remark for rejecting this expense.
            </DialogDescription>
          </DialogHeader>
          <FieldGroup>
            <FormField
              form={form}
              name="remark"
              label="Remark"
              type="text"
              placeholder="Enter rejection reason..."
            />
          </FieldGroup>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isPending}
              form="reject-expense-form"
              text="Reject"
            />
          </DialogFooter>
        </DialogContent>
      </form>
    </Dialog>
  );
}
