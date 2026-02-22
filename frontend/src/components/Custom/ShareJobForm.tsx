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
  DialogTrigger,
} from "@/components/ui/dialog";
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import { useShareJob } from "@/api/queries/useJob";

const formSchema = z.object({
  email: z.email("Invalid email input"),
});

export function ShareJobForm() {
  const { jobId } = useParams();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
    },
  });

  const { mutate: shareJob, isPending } = useShareJob();

  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log(data);
    console.log(jobId);
    shareJob({ id: jobId, data: { email: data.email, jobId } });
  }

  const navigate = useNavigate();
  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      <form id="form-rhf-demo" onSubmit={form.handleSubmit(onSubmit)}>
        {/* <DialogTrigger
          asChild
          className="w-full pointer-events-auto"
          onClick={(e) => {
            e.stopPropagation();
            navigate(`${jobId}/share`);
          }}
        >
          <Button variant="secondary">Share Job</Button>
        </DialogTrigger> */}
        <DialogContent
          className="sm:max-w-sm pointer-events-auto"
          onClick={(e) => {
            e.stopPropagation();
          }}
        >
          <DialogHeader>
            <DialogTitle>Share Job</DialogTitle>
            <DialogDescription>
              Enter email address of the person with whom you want to share
            </DialogDescription>
          </DialogHeader>
          <FieldGroup>
            <FormField
              form={form}
              name="email"
              label="Email"
              type="text"
              placeholder="something@roimaint.com"
            />
          </FieldGroup>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isPending}
              form="form-rhf-demo"
              text="Submit"
            />
          </DialogFooter>
        </DialogContent>
      </form>
    </Dialog>
  );
}
