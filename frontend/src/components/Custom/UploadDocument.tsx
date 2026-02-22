import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";

import { Controller } from "react-hook-form";
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
import {
  Field,
  FieldDescription,
  FieldError,
  FieldLabel,
} from "@/components/ui/field";
import { Textarea } from "@/components/ui/textarea";
import { FieldGroup } from "@/components/ui/field";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import FileFormField from "./FileFormField";
import DocumentTypeComboboxWrapper from "../wrappers/DocumentTypeCombobxWrapper";
import { useUploadTravelDocument } from "@/api/queries/useTravel";
import { useState } from "react";

const formSchema = z.object({
  documentReqDto: z.object({
    fkDocumentTypeId: z.string().min(1, "Document type is required"),
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
});

export function UploadDocument({ name }) {
  const { userTravelId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      documentReqDto: {
        fkDocumentTypeId: "",
        file: undefined,
      },
    },
  });

  const {
    isPending,
    mutate: uploadTravelDocument,
    isSuccess,
  } = useUploadTravelDocument(userTravelId);

  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log("ad");
    const formData = new FormData();
    formData.append("userTravelId", userTravelId);
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append(
      "documentReqDto.fkDocumentTypeId",
      data.documentReqDto.fkDocumentTypeId,
    );
    uploadTravelDocument(
      { id: userTravelId, documentDto: formData },
      { onSuccess: () => navigate(-1) },
    );
  }
  const navigate = useNavigate();
  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      {/* <DialogTrigger
        asChild
        onClick={(e) => {
          e.stopPropagation();
        }}
      >
        <Button variant="default">Upload files</Button>
      </DialogTrigger> */}
      <DialogContent className="sm:max-w-sm ">
        <DialogHeader>
          <DialogTitle>Upload Document</DialogTitle>
          <DialogDescription>Upload document for {name}</DialogDescription>
        </DialogHeader>
        <form
          id="form-rhf-demo"
          onSubmit={form.handleSubmit(onSubmit)}
          encType="multipart/form-data"
        >
          <FieldGroup>
            <DocumentTypeComboboxWrapper
              form={form}
              name={"documentReqDto.fkDocumentTypeId"}
            />
            <FileFormField
              form={form}
              name={"documentReqDto.file"}
              label={"Upload files"}
              placeholder={"Document"}
            />
          </FieldGroup>
          <DialogFooter className="mt-10">
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner isPending={isPending} text="Submit" />
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
