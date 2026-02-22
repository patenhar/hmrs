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
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import { useReferJob } from "@/api/queries/useJob";
import FileFormField from "./FileFormField";

const formSchema = z.object({
  name: z.string().min(1, "Name is required"),
  email: z.email(),
  note: z.string().min(1, "Note is required"),
  documentReqDto: z.object({
    fkDocumentTypeId: z.string(),
    file: z.any().refine((file) => file instanceof File, {
      message: "File is required",
    }),
  }),
});

export function ReferJobForm() {
  const { jobId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      email: "",
      note: "",
      documentReqDto: {
        fkDocumentTypeId: "f8eb8f11-33e1-4f4f-b248-1b755aed16a0",
        file: undefined,
      },
    },
  });

  const { mutate: referJob, isPending } = useReferJob();

  function onSubmit(data: z.infer<typeof formSchema>) {
    const formData = new FormData();
    formData.append("name", data.name);
    formData.append("email", data.email);
    formData.append("note", data.note);
    formData.append("documentReqDto.file", data.documentReqDto.file);
    formData.append(
      "documentReqDto.fkDocumentTypeId",
      data.documentReqDto.fkDocumentTypeId,
    );

    referJob({ id: jobId, data: formData });
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
          navigate(`${jobId}/refer`);
        }}
      >
        <Button variant="default">Refer Job</Button>
      </DialogTrigger> */}
      <DialogContent className="sm:max-w-sm ">
        <DialogHeader>
          <DialogTitle>Refer Job</DialogTitle>
          <DialogDescription>
            Enter details of the person, you want to refer
          </DialogDescription>
        </DialogHeader>
        <form
          id="form-rhf-demo"
          onSubmit={form.handleSubmit(onSubmit)}
          encType="multipart/form-data"
        >
          <FieldGroup>
            <FormField
              form={form}
              name="name"
              label="Name"
              type="text"
              placeholder="Name"
            />
            <FormField
              form={form}
              name="email"
              label="Email"
              type="text"
              placeholder="something@roimaint.com"
            />
            <FormField
              form={form}
              name="note"
              label="Note"
              type="text"
              placeholder="Note"
            />
            {/* <Controller
              name="note"
              control={form.control}
              render={({ field, fieldState }) => (
                <Field data-invalid={fieldState.invalid}>
                  <FieldLabel htmlFor="note">Note</FieldLabel>
                  <Textarea
                    {...field}
                    id="note"
                    placeholder="Enter some note here"
                    aria-invalid={fieldState.invalid}
                  />
                  {fieldState.invalid && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            /> */}
            <FileFormField
              form={form}
              name={"documentReqDto.file"}
              label={"Upload resume"}
              placeholder={"resume"}
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
