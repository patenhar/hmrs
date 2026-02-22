import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
import * as z from "zod";

import { useJobStakeHolderTypes } from "@/api/queries/useJobStakeHolders";

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
import { Trash } from "lucide-react";
import { AsyncSingleCombobox } from "./AsyncSingleCombobox";
import { useUser } from "@/api/queries/useUser";
import FileFormField from "./FileFormField";
import { useNavigate } from "react-router-dom";
import UserComboboxWrapper from "../wrappers/UserComboboxWrapper";
import JobStakeHolderTypeComboboxWrapper from "../wrappers/JobStakeHolderTypeComboboxWrapper";
import { useRegister } from "@/api/queries/useAuth";
import { useAddJob } from "@/api/queries/useJob";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  tempUserId: z.string().optional(),
  tempType: z.string().optional(),
  jobStakeHolderReqDtos: z
    .array(
      z.object({
        userId: z.string(),
        jobStakeHolderTypeId: z.string(),
      }),
    )
    .min(1, "At least one stakeholder is required"),
  documentReqDto: z.object({
    fkDocumentTypeId: z.string(),
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
});

export function AddJobForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: "",
      description: "",
      tempUserId: "",
      tempType: "",
      jobStakeHolderReqDtos: [],
      documentReqDto: {
        fkDocumentTypeId: "f8eb8f11-33e1-4f4f-b248-1b755aed16a0",
        file: undefined,
      },
    },
  });

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "jobStakeHolderReqDtos",
  });

  const { mutate: addJob, isPending } = useAddJob();

  function onSubmit(data: z.infer<typeof formSchema>) {
    const formData = new FormData();
    formData.append("title", data.title);
    formData.append("description", data.description);
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append(
      "documentReqDto.fkDocumentTypeId",
      data.documentReqDto.fkDocumentTypeId,
    );
    data.jobStakeHolderReqDtos.forEach((dto, idx) => {
      formData.append(`jobStakeHolderReqDtos[${idx}].userId`, dto.userId);
      formData.append(
        `jobStakeHolderReqDtos[${idx}].jobStakeHolderTypeId`,
        dto.jobStakeHolderTypeId,
      );
    });
    addJob(formData);
  }
  const { isLoading: userLoading, data: userData } = useUser("");
  const { isLoading, data } = useJobStakeHolderTypes("");

  const userMap = () => {
    const map = new Map();
    userData?.data.data.forEach((u) => map.set(u.pkUserId, u));
    return map;
  };

  const typeMap = () => {
    const map = new Map();
    data?.data.data.forEach((u) => map.set(u.pkJobStakeHolderTypeId, u));
    return map;
  };

  function addStakeHolder() {
    event?.preventDefault();
    const userId = form.getValues("tempUserId");
    const typeId = form.getValues("tempType");

    if (!userId || !typeId) {
      return;
    }

    const exists = fields.some(
      (f) => f.userId === userId && f.jobStakeHolderTypeId === typeId,
    );
    if (exists) return;
    append({
      userId,
      jobStakeHolderTypeId: typeId,
    });
    form.setValue("tempUserId", "");
    form.setValue("tempType", "");
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
        <Button variant="outline">Add Job</Button>
      </DialogTrigger> */}
      <DialogContent className="w-full max-w-[90vw] lg:max-w-3xl">
        <DialogHeader>
          <DialogTitle>Add Job</DialogTitle>
          <DialogDescription>
            Fill in the below details to add new job post
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
                name={"title"}
                label={"Title"}
                type="text"
                placeholder={"Title"}
              />
              <UserComboboxWrapper
                disabled={false}
                form={form}
                name={"tempUserId"}
              />
              <FileFormField
                form={form}
                name={"documentReqDto.file"}
                label={"JD"}
                placeholder={"JD"}
              />
            </FieldGroup>
            <FieldGroup className="space-y-4">
              <FormField
                form={form}
                name={"description"}
                label={"Description"}
                type="text"
                placeholder={"Description"}
              />
              <div className="flex justify-between gap-4 items-end">
                <JobStakeHolderTypeComboboxWrapper
                  disabled={false}
                  form={form}
                  name={"tempType"}
                />
                <Button
                  type="butoon"
                  onClick={addStakeHolder}
                  disabled={
                    !form.watch("tempUserId") || !form.watch("tempType")
                  }
                >
                  Add
                </Button>
              </div>
            </FieldGroup>
          </div>
          <Table>
            <TableCaption>
              List selected of stack holders for this job
            </TableCaption>
            <TableHeader>
              <TableRow>
                <TableHead className="w-[325px]">Email</TableHead>
                <TableHead className="w-[100px]">Role</TableHead>
                <TableHead className="w-[50px] text-right">Action</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {fields.map((field, index) => {
                const user = userMap().get(field.userId);
                const type = typeMap().get(field.jobStakeHolderTypeId);

                return (
                  <TableRow
                    key={`${user?.userId} + ${type?.pkJobStakeHolderTypeId}`}
                  >
                    <TableCell className="font-medium">{user?.email}</TableCell>
                    <TableCell>{type?.jobStakeHolderTypeName}</TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => {
                          remove(index);
                        }}
                      >
                        <Trash className="h-4 w-4 text-red-500" />
                      </Button>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
          <DialogFooter className="col-span-1 md:col-span-2 flex justify-end gap-3">
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isPending}
              form="form-rhf-demo"
              text="Submit"
            />
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
