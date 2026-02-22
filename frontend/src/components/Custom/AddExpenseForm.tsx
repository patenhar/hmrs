import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
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
  DialogTrigger,
} from "@/components/ui/dialog";
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useState } from "react";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Plus, Trash } from "lucide-react";
import { AsyncSingleCombobox } from "./AsyncSingleCombobox";
import { GetUserById, useUser } from "@/api/queries/useUser";
import { DatePickerWithRange } from "./DatePickerWithRange";
import { useRegister } from "@/api/queries/useAuth";
import CityComboboxWrapper from "../wrappers/CityComboboxWrapper";
import CountryComboboxWrapper from "../wrappers/CountryComboboxWrapper";
import { useCreateTravel } from "@/api/queries/useTravel.ts";
import FileFormField from "./FileFormField";
import ExpenseTypeComboboxWrapper from "../wrappers/ExpenseTypeComboboxWrapper";
import { useAddExpense } from "@/api/queries/useExpense";
import { useNavigate, useParams } from "react-router-dom";

const formSchema = z.object({
  description: z.string().min(1, "Description is required"),
  amount: z.coerce.number().min(1, "Amount cannot be zero"),
  documentReqDto: z.object({
    fkDocumentTypeId: z.string(),
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
  expenseTypeId: z.string().min(1, "User travel id is required"),
});

export function AddExpenseForm() {
  const { userTravelId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      description: "",
      amount: 0,
      documentReqDto: {
        fkDocumentTypeId: "f8eb8f11-33e1-4f4f-b248-1b755aed16a0",
        file: undefined,
      },
      expenseTypeId: "",
    },
  });

  const { mutate: addExpense, isPending } = useAddExpense();

  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log(data);
    console.log(userTravelId);

    const formData = new FormData();
    formData.append("description", data.description);
    formData.append("amount", data.amount);
    formData.append("userTravelId", userTravelId);
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append(
      "documentReqDto.fkDocumentTypeId",
      data.documentReqDto.fkDocumentTypeId,
    );
    formData.append("expenseTypeId", data.expenseTypeId);

    addExpense(formData);
  }

  const navigate = useNavigate();

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      {/* <DialogTrigger asChild>
        <Button variant="outline">Add Expense</Button>
      </DialogTrigger> */}
      <DialogContent className="w-full max-w-[90vw] lg:max-w-3xl">
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
            <FieldGroup className="space-y-4">
              <FormField
                form={form}
                name={"description"}
                label={"Description"}
                type="text"
                placeholder={"Description"}
              />
              <FileFormField
                form={form}
                name={"documentReqDto.file"}
                label={"Proof"}
                placeholder={"Proof"}
              />
            </FieldGroup>
            <FieldGroup className="space-y-4">
              <FormField
                form={form}
                name={"amount"}
                label={"Amount"}
                type="number"
                placeholder={"10000"}
              />
              <ExpenseTypeComboboxWrapper
                disabled={false}
                form={form}
                name={"expenseTypeId"}
              />
            </FieldGroup>
            <DialogFooter className="col-span-1 md:col-span-2 flex justify-end gap-3">
              <DialogClose asChild>
                <Button variant="outline">Cancel</Button>
              </DialogClose>
              <ButtonSpinner
                //   isPending={isPending}
                text="Submit"
              />
            </DialogFooter>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
