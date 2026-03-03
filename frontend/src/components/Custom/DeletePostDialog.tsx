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
import { useDeletePost } from "@/api/queries/usePost";

const formSchema = z.object({
  remarks: z.string().min(1, "Reason is required"),
});

interface DeletePostDialogProps {
  postId: string | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function DeletePostDialog({
  postId,
  open,
  onOpenChange,
}: Readonly<DeletePostDialogProps>) {
  const { mutate: deletePost, isPending } = useDeletePost();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { remarks: "" },
  });

  function onSubmit(data: z.infer<typeof formSchema>) {
    if (!postId) return;
    deletePost(
      { id: postId, remarks: data.remarks },
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
      <DialogContent className="sm:max-w-sm">
        <DialogHeader>
          <DialogTitle>Remove Post</DialogTitle>
          <DialogDescription>
            Please provide a reason for removing this post. The author will be
            notified by email.
          </DialogDescription>
        </DialogHeader>
        <form id="delete-post-form" onSubmit={form.handleSubmit(onSubmit)}>
          <FieldGroup>
            <FormField
              form={form}
              name="remarks"
              label="Reason"
              type="text"
              placeholder="Enter reason for removal..."
            />
          </FieldGroup>
        </form>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancel</Button>
          </DialogClose>
          <ButtonSpinner
            isPending={isPending}
            form="delete-post-form"
            text="Remove"
          />
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
