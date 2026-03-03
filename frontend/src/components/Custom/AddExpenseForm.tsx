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
import FileFormField from "./FileFormField";
import { useAddExpense, useGetExpenseType } from "@/api/queries/useExpense";
import { useNavigate, useParams } from "react-router-dom";
import AsyncCombobox from "./AsyncCombobox";

const formSchema = z.object({
  description: z.string().min(1, "Description is required"),
  amount: z.coerce.number().min(1, "Amount cannot be zero"),
  documentReqDto: z.object({
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
  expenseType: z
    .object({
      pkExpenseTypeId: z.string(),
      expenseTypeName: z.string(),
    })
    .nullable()
    .refine((data) => data != null, {
      message: "Expense type is required",
    }),
});

export function AddExpenseForm() {
  const { userTravelId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      description: "",
      amount: 0,
      documentReqDto: {
        file: undefined,
      },
      expenseType: null,
    },
  });

  const { mutate: addExpense, isPending } = useAddExpense();

  function onSubmit(data: z.infer<typeof formSchema>) {
    const formData = new FormData();
    formData.append("description", data.description);
    formData.append("amount", data.amount);
    formData.append("userTravelId", userTravelId);
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append("documentReqDto.documentType", "OTHER");
    formData.append("expenseType", data.expenseType!.pkExpenseTypeId);

    addExpense(formData, {
      onSuccess: () => {
        navigate(-1);
      },
    });
  }

  const navigate = useNavigate();

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      <DialogContent className="w-full max-w-[90vw] lg:max-w-2xl">
        <DialogHeader>
          <DialogTitle>Add Expense</DialogTitle>
          <DialogDescription>
            Fill in the below details to apply for expense
          </DialogDescription>
        </DialogHeader>
        <form
          id="form-rhf-demo"
          onSubmit={form.handleSubmit(onSubmit)}
          encType="multipart/form-data"
        >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField
              form={form}
              name={"description"}
              label={"Description"}
              type="text"
              placeholder={"Description"}
            />
            <FormField
              form={form}
              name={"amount"}
              label={"Amount"}
              type="number"
              placeholder={"10000"}
            />
            <FileFormField
              form={form}
              name={"documentReqDto.file"}
              label={"Proof"}
              placeholder={"Proof"}
            />
            <AsyncCombobox
              disabled={false}
              form={form}
              name={"expenseType"}
              label={"Expense Type"}
              placeholder={"Select expense type"}
              fetchFunction={useGetExpenseType}
              displayKey={"expenseTypeName"}
              primaryKey={"pkExpenseTypeId"}
            />
            <DialogFooter className="col-span-1 md:col-span-2 flex justify-end gap-3">
              <DialogClose asChild>
                <Button variant="outline">Cancel</Button>
              </DialogClose>
              <ButtonSpinner
                form="form-rhf-demo"
                isPending={isPending}
                text="Submit"
              />
            </DialogFooter>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
