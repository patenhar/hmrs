import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, Controller } from "react-hook-form";
import * as z from "zod";
import { useState, KeyboardEvent, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import postVisibilityService from "@/api/postVisibilityService";
import { useCreatePost } from "@/api/queries/usePost";
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
import { useNavigate } from "react-router-dom";
import { X } from "lucide-react";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  tags: z.array(z.string()).optional(),
  visibilityId: z.string().min(1, "Visibility is required"),
});

export function AddPostForm() {
  const navigate = useNavigate();
  const [tagInput, setTagInput] = useState("");

  const { data: visibilityData } = useQuery({
    queryKey: ["PostVisibility"],
    queryFn: () => postVisibilityService.getAllVisibilities(),
  });
  const visibilities = visibilityData?.data.data ?? [];

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { title: "", description: "", tags: [], visibilityId: "" },
  });

  useEffect(() => {
    if (visibilities.length > 0 && !form.getValues("visibilityId")) {
      const allEmployees = visibilities.find(
        (v) => v.visibility === "ALL_EMPLOYEES",
      );
      const target = allEmployees ?? visibilities[0];
      form.setValue("visibilityId", target.pkPostVisibilityId);
    }
  }, [visibilities]);

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

  const { mutate: createPost, isPending } = useCreatePost();

  function onSubmit(data: z.infer<typeof formSchema>) {
    createPost(
      {
        title: data.title,
        description: data.description,
        tags: data.tags ?? [],
        visibilityId: data.visibilityId,
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
          <DialogTitle>Create Achievement Post</DialogTitle>
        </DialogHeader>

        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <FieldGroup>
            <FormField
              form={form}
              name="title"
              label="Title"
              placeholder="What did you achieve?"
            />

            <Controller
              name="description"
              control={form.control}
              render={({ field, fieldState }) => (
                <Field data-invalid={fieldState.invalid}>
                  <FieldLabel>Description</FieldLabel>
                  <Textarea
                    {...field}
                    placeholder="Share the details of your achievement..."
                    className="min-h-[100px]"
                  />
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
              {isPending ? <ButtonSpinner /> : "Post"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
