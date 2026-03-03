import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, Controller } from "react-hook-form";
import * as z from "zod";
import { useState, KeyboardEvent, useEffect } from "react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Field,
  FieldError,
  FieldLabel,
  FieldGroup,
} from "@/components/ui/field";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import { useGetPostById, useUpdatePost } from "@/api/queries/usePost";
import { Spinner } from "@/components/ui/spinner";
import { X } from "lucide-react";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  tags: z.array(z.string()).optional(),
});

export function EditPostForm() {
  const { postId } = useParams<{ postId: string }>();
  const navigate = useNavigate();
  const { data: postData, isLoading } = useGetPostById(postId ?? "");
  const post = postData?.data.data;
  const [tagInput, setTagInput] = useState("");

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { title: "", description: "", tags: [] },
  });

  useEffect(() => {
    if (post) {
      form.reset({
        title: post.title,
        description: post.description,
        tags: post.tags?.map((t) => t.tag) ?? [],
      });
    }
  }, [post]);

  function addTag(raw: string) {
    const name = raw.trim().replace(/^#+/, "").toLowerCase();
    if (!name) return;
    const current = form.getValues("tags") ?? [];
    if (!current.includes(name)) form.setValue("tags", [...current, name]);
    setTagInput("");
  }

  function handleTagKeyDown(e: KeyboardEvent<HTMLInputElement>) {
    if (e.key === "Enter" || e.key === "," || e.key === " ") {
      e.preventDefault();
      addTag(tagInput);
    } else if (e.key === "Backspace" && tagInput === "") {
      const current = form.getValues("tags") ?? [];
      form.setValue("tags", current.slice(0, -1));
    }
  }

  const { mutate: updatePost, isPending } = useUpdatePost();

  function onSubmit(data: z.infer<typeof formSchema>) {
    if (!postId) return;
    updatePost(
      {
        id: postId,
        data: {
          title: data.title,
          description: data.description,
          tags: data.tags ?? [],
        },
      },
      { onSuccess: () => navigate(-1) },
    );
  }

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      <DialogContent className="sm:max-w-[520px]">
        <DialogHeader>
          <DialogTitle>Edit Post</DialogTitle>
        </DialogHeader>

        {isLoading ? (
          <Spinner />
        ) : (
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FieldGroup>
              <FormField form={form} name="title" label="Title" />

              <Controller
                name="description"
                control={form.control}
                render={({ field, fieldState }) => (
                  <Field data-invalid={fieldState.invalid}>
                    <FieldLabel>Description</FieldLabel>
                    <Textarea {...field} className="min-h-[100px]" />
                    {fieldState.invalid && (
                      <FieldError errors={[fieldState.error]} />
                    )}
                  </Field>
                )}
              />

              <Controller
                name="tags"
                control={form.control}
                render={({ field }) => (
                  <Field>
                    <FieldLabel>Tags</FieldLabel>
                    <div className="flex flex-wrap items-center gap-1.5 min-h-9 w-full rounded-md border border-input bg-background px-3 py-1.5 text-sm focus-within:ring-2 focus-within:ring-ring">
                      {(field.value ?? []).map((tag) => (
                        <span
                          key={tag}
                          className="flex items-center gap-1 bg-blue-100 text-blue-700 text-xs font-medium px-2 py-0.5 rounded-full"
                        >
                          #{tag}
                          <button
                            type="button"
                            onClick={() =>
                              field.onChange(
                                (field.value ?? []).filter((t) => t !== tag),
                              )
                            }
                          >
                            <X className="w-3 h-3" />
                          </button>
                        </span>
                      ))}
                      <Input
                        value={tagInput}
                        onChange={(e) => setTagInput(e.target.value)}
                        onKeyDown={handleTagKeyDown}
                        onBlur={() => addTag(tagInput)}
                        placeholder={
                          (field.value ?? []).length === 0
                            ? "#cricket, #teamwork..."
                            : ""
                        }
                        className="border-0 p-0 h-auto flex-1 min-w-[100px] focus-visible:ring-0 shadow-none"
                      />
                    </div>
                    <p className="text-xs text-muted-foreground mt-1">
                      Press Enter, comma, or space to add a tag
                    </p>
                  </Field>
                )}
              />
            </FieldGroup>

            <DialogFooter>
              <DialogClose asChild>
                <Button variant="outline" type="button">
                  Cancel
                </Button>
              </DialogClose>
              <Button type="submit" disabled={isPending}>
                {isPending ? <ButtonSpinner /> : "Save changes"}
              </Button>
            </DialogFooter>
          </form>
        )}
      </DialogContent>
    </Dialog>
  );
}
