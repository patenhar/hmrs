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
import { useNavigate } from "react-router-dom";
import { useCreatePost } from "@/api/queries/usePost";
import { useGetAllTags } from "@/api/queries/useTag";
import { useQuery } from "@tanstack/react-query";
import postVisibilityService from "@/api/postVisibilityService";
import { useEffect } from "react";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  tagIds: z.array(z.string()).optional(),
  visibilityId: z.string().min(1, "Visibility is required"),
});

export function AddPostForm() {
  const navigate = useNavigate();
  const { data: tagsData } = useGetAllTags();
  const tags = tagsData?.data.data ?? [];

  const { data: visibilityData } = useQuery({
    queryKey: ["PostVisibility"],
    queryFn: () => postVisibilityService.getAllVisibilities(),
  });
  const visibilities = visibilityData?.data.data ?? [];

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: "",
      description: "",
      tagIds: [],
      visibilityId: "",
    },
  });

  // Set default visibility to "ALL_EMPLOYEES" once loaded
  useEffect(() => {
    if (visibilities.length > 0 && !form.getValues("visibilityId")) {
      const allEmployees = visibilities.find(
        (v) => v.visibility === "ALL_EMPLOYEES",
      );
      if (allEmployees)
        form.setValue("visibilityId", allEmployees.pkPostVisibilityId);
    }
  }, [visibilities]);

  const { mutate: createPost, isPending } = useCreatePost();

  function onSubmit(data: z.infer<typeof formSchema>) {
    createPost(
      {
        title: data.title,
        description: data.description,
        tagIds: data.tagIds ?? [],
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
              {isPending ? <ButtonSpinner /> : "Post"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
