import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
import * as z from "zod";
import { useState } from "react";

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
} from "@/components/ui/dialog";
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
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
import { useUser } from "@/api/queries/useUser";
import FileFormField from "./FileFormField";
import { useNavigate } from "react-router-dom";
import { useAddJob, useUpdateJob } from "@/api/queries/useJob";
import AsyncCombobox from "./AsyncCombobox";
import { toast } from "sonner";

const getFormSchema = (isUpdate: boolean) =>
  z.object({
    title: z.string().min(1, "Title is required"),
    description: z.string().min(1, "Description is required"),
    jobStakeHolderReqDtos: z
      .array(
        z.object({
          userId: z.object({
            pkUserId: z.string(),
            email: z.string(),
          }),
          jobStakeHolderTypeId: z.object({
            pkJobStakeHolderTypeId: z.string(),
            jobStakeHolderTypeName: z.string(),
          }),
        }),
      )
      .min(1, "At least one stakeholder is required"),
    tempUser: z.object().nullable().optional(),
    tempType: z.object().nullable().optional(),
    documentReqDto: z.object({
      fkDocumentTypeId: z.string(),
      file: isUpdate
        ? z.any().optional()
        : z.any().refine((f) => f instanceof File, {
            message: "File is required",
          }),
    }),
  });

export function AddJobForm({
  currentData,
  isUpdate = false,
}: {
  currentData?: any;
  isUpdate?: boolean;
}) {
  console.log(currentData);
  const formSchema = getFormSchema(isUpdate);
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: currentData
      ? {
          title: currentData.title,
          description: currentData.description,
          jobStakeHolderReqDtos:
            currentData.jobStakeHolders?.map((s) => ({
              userId: {
                pkUserId: s.user.pkUserId,
                email: s.user.email,
              },
              jobStakeHolderTypeId: {
                pkJobStakeHolderTypeId: s.jobStakeHolderType,
                jobStakeHolderTypeName: String(s.jobStakeHolderType)
                  .replace(/_/g, " ")
                  .replace(/\b\w/g, (l) => l.toUpperCase()),
              },
            })) ?? [],
          documentReqDto: {
            fkDocumentTypeId: "JOB_DESCRIPTION",
            file: undefined,
          },
        }
      : {
          title: "",
          description: "",
          jobStakeHolderReqDtos: [],
          documentReqDto: {
            fkDocumentTypeId: "JOB_DESCRIPTION",
            file: undefined,
          },
        },
  });

  const { append, remove } = useFieldArray({
    control: form.control,
    name: "jobStakeHolderReqDtos",
  });

  const { mutate: addJob, isPending: addPending } = useAddJob();
  const { mutate: updateJob, isPending: updatePending } = useUpdateJob();

  function onSubmit(data: z.infer<typeof formSchema>) {
    const formData = new FormData();
    formData.append("title", data.title);
    formData.append("description", data.description);
    if (data.documentReqDto.file instanceof File) {
      formData.append("documentReqDto.file", data.documentReqDto.file);
      formData.append(
        "documentReqDto.documentType",
        data.documentReqDto.fkDocumentTypeId,
      );
    }
    data.jobStakeHolderReqDtos.forEach((dto, idx) => {
      formData.append(
        `jobStakeHolderReqDtos[${idx}].userId`,
        dto.userId.pkUserId,
      );
      formData.append(
        `jobStakeHolderReqDtos[${idx}].jobStakeHolderType`,
        dto.jobStakeHolderTypeId.pkJobStakeHolderTypeId,
      );
    });
    if (isUpdate && currentData?.pkJobId) {
      updateJob(
        { jobId: currentData.pkJobId, data: formData },
        {
          onSuccess: () => {
            navigate(-1);
          },
        },
      );
    } else {
      addJob(formData, {
        onSuccess: () => {
          navigate(-1);
        },
      });
    }
  }

  function addStakeHolder() {
    event?.preventDefault();
    const user = form.watch("tempUser");
    const type = form.watch("tempType");

    if (!user || !type) return;

    const exists = form
      .watch("jobStakeHolderReqDtos")
      .some(
        (js) =>
          js.userId.pkUserId === user.pkUserId &&
          js.jobStakeHolderTypeId.pkJobStakeHolderTypeId ===
            type.pkJobStakeHolderTypeId,
      );

    if (exists) {
      toast.error("This stakeholder is already added");
      return;
    }

    append({
      userId: user,
      jobStakeHolderTypeId: type,
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
      <DialogContent className="w-full max-w-[90vw] lg:max-w-3xl">
        <DialogHeader>
          <DialogTitle>{isUpdate ? "Update Job" : "Add Job"}</DialogTitle>
          <DialogDescription>
            Fill in the below details to {isUpdate ? "update the" : "add new"}{" "}
            job post
          </DialogDescription>
        </DialogHeader>
        <form
          id="form-rhf-demo"
          onSubmit={form.handleSubmit(onSubmit)}
          encType="multipart/form-data"
        >
          <div className="no-scrollbar -mx-4 max-h-[50vh] overflow-y-auto px-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FormField
                form={form}
                name={"title"}
                label={"Title"}
                type="text"
                placeholder={"Title"}
              />
              <FormField
                form={form}
                name={"description"}
                label={"Description"}
                type="text"
                placeholder={"Description"}
              />
              <AsyncCombobox
                single={true}
                disabled={false}
                form={form}
                name={"tempUser"}
                label={"User"}
                placeholder={"Select stakeholder for this job"}
                fetchFunction={useUser}
                displayKey={"email"}
                primaryKey={"pkUserId"}
              />
              <div className="flex justify-between gap-4 items-end">
                <AsyncCombobox
                  single={true}
                  disabled={false}
                  form={form}
                  name={"tempType"}
                  label={"Job stake holder type"}
                  placeholder={"Select job stake holder type"}
                  fetchFunction={useJobStakeHolderTypes}
                  displayKey={"jobStakeHolderTypeName"}
                  primaryKey={"pkJobStakeHolderTypeId"}
                />
                <Button
                  type="button"
                  onClick={addStakeHolder}
                  disabled={!form.watch("tempUser") || !form.watch("tempType")}
                >
                  Add
                </Button>
              </div>
              <FileFormField
                form={form}
                name={"documentReqDto.file"}
                label={"JD"}
                placeholder={"JD"}
              />
            </div>
            <Table className="mt-8">
              <TableCaption>
                List selected of stack holders for this job
              </TableCaption>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead>Role</TableHead>
                  <TableHead className="w-[50px] text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {form.watch("jobStakeHolderReqDtos").map((js, index) => {
                  return (
                    <TableRow
                      key={`${js.userId.pkUserId}-${js.jobStakeHolderTypeId.pkJobStakeHolderTypeId}`}
                    >
                      <TableCell>{js.userId.profile?.name}</TableCell>
                      <TableCell className="font-medium">
                        {js.userId.email}
                      </TableCell>
                      <TableCell className="font-medium">
                        {js.jobStakeHolderTypeId?.jobStakeHolderTypeName ??
                          js.jobStakeHolderTypeId?.pkJobStakeHolderTypeId}
                      </TableCell>
                      <TableCell className="text-right">
                        <Button
                          type="button"
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
          </div>
          <DialogFooter className="col-span-1 md:col-span-2 flex justify-end gap-3">
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isUpdate ? updatePending : addPending}
              form="form-rhf-demo"
              text="Submit"
            />
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
