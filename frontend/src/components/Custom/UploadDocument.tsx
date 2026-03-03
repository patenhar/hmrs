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
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import FileFormField from "./FileFormField";
import AsyncCombobox from "./AsyncCombobox";
import { useGetDocumentTypes } from "@/api/queries/useDocument";
import { useUploadTravelDocument } from "@/api/queries/useTravel";

const formSchema = z.object({
  documentReqDto: z.object({
    documentType: z
      .object({ pkDocumentTypeId: z.string(), documentTypeName: z.string() })
      .nullable()
      .refine((v) => v !== null, { message: "Document type is required" }),
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
});

export function UploadDocument({ name }: Readonly<{ name: string }>) {
  const { userTravelId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      documentReqDto: {
        documentType: null,
        file: undefined,
      },
    },
  });

  const { isPending, mutate: uploadTravelDocument } =
    useUploadTravelDocument(userTravelId);

  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log("ad");
    const formData = new FormData();
    formData.append("userTravelId", userTravelId ?? "");
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append(
      "documentReqDto.documentType",
      data.documentReqDto.documentType!.pkDocumentTypeId,
    );
    uploadTravelDocument(
      { id: userTravelId, documentDto: formData },
      {
        onSuccess: () => {
          navigate(-1);
        },
      },
    );
  }
  const navigate = useNavigate();
  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) {
          navigate(-1);
        }
      }}
    >
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
            <AsyncCombobox
              single={true}
              form={form}
              name={"documentReqDto.documentType"}
              label={"Document type"}
              placeholder={"Select document type"}
              fetchFunction={useGetDocumentTypes}
              displayKey={"documentTypeName"}
              primaryKey={"pkDocumentTypeId"}
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
