import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, Controller } from "react-hook-form";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { FieldGroup } from "@/components/ui/field";
import { Textarea } from "@/components/ui/textarea";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate, useParams } from "react-router-dom";
import { useGetPostById, useUpdatePost } from "@/api/queries/usePost";
import { useGetAllTags } from "@/api/queries/useTag";
import { useEffect } from "react";
import { Spinner } from "@/components/ui/spinner";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  tagIds: z.array(z.string()).optional(),
  visibilityId: z.string(),
});

export function EditPostForm() {
  const { postId } = useParams<{ postId: string }>();
  const navigate = useNavigate();
  const { data: postData, isLoading } = useGetPostById(postId ?? "");
  const { data: tagsData } = useGetAllTags();
  const post = postData?.data.data;
  const tags = tagsData?.data.data ?? [];

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { title: "", description: "", tagIds: [], visibilityId: "" },
  });

  useEffect(() => {
    if (post) {
      form.reset({
        title: post.title,
        description: post.description,
        tagIds: post.tags?.map((t) => t.pkTagId) ?? [],
        visibilityId: post.visibility?.pkPostVisibilityId ?? "",
      });
    }
  }, [post]);

  const { mutate: updatePost, isPending } = useUpdatePost();

  function onSubmit(data: z.infer<typeof formSchema>) {
    if (!postId) return;
    updatePost(
      {
        id: postId,
        data: {
          title: data.title,
          description: data.description,
          tagIds: data.tagIds ?? [],
          visibilityId: data.visibilityId,
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

              {tags.length > 0 && (
                <Controller
                  name="tagIds"
                  control={form.control}
                  render={({ field }) => (
                    <Field>
                      <FieldLabel>Tags</FieldLabel>
                      <div className="flex flex-wrap gap-2">
                        {tags.map((tag) => {
                          const selected = field.value?.includes(tag.pkTagId);
                          return (
                            <button
                              key={tag.pkTagId}
                              type="button"
                              onClick={() => {
                                const current = field.value ?? [];
                                field.onChange(
                                  selected
                                    ? current.filter((id) => id !== tag.pkTagId)
                                    : [...current, tag.pkTagId],
                                );
                              }}
                              className={`px-3 py-1 rounded-full text-xs border transition-colors ${
                                selected
                                  ? "bg-blue-600 text-white border-blue-600"
                                  : "bg-white text-gray-600 border-gray-300 hover:border-blue-400"
                              }`}
                            >
                              {tag.tag}
                            </button>
                          );
                        })}
                      </div>
                    </Field>
                  )}
                />
              )}
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
