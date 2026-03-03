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
import { useNavigate, useParams } from "react-router-dom";
import { useReferJob } from "@/api/queries/useJob";
import FileFormField from "./FileFormField";

const formSchema = z.object({
  name: z.string().min(1, "Name is required"),
  email: z.email(),
  note: z.string().min(1, "Note is required"),
  documentReqDto: z.object({
    documentType: z.string(),
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
        documentType: "JOB_DESCRIPTION",
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
      "documentReqDto.documentType",
      data.documentReqDto.documentType,
    );

    referJob(
      { id: jobId, data: formData },
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
