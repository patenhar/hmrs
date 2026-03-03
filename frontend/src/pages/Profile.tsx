import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Field, FieldGroup } from "@/components/ui/field";
import { useCreateProfile } from "@/api/queries/useProfile.tsx";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner.tsx";
import FormField from "@/components/Custom/FormField.tsx";
import { DatePicker } from "@/components/Custom/DatePicker.tsx";
import { useUser } from "@/api/queries/useUser.tsx";
import AsyncCombobox from "@/components/Custom/AsyncCombobox.tsx";
import { useGetDepartments } from "@/api/queries/useDepartment.tsx";

const formSchema = z.object({
  name: z.string().min(1, "Name is required"),
  birthDate: z
    .date("Birth date is required")
    .max(new Date(), "Birth date must be in the past"),
  joiningDate: z
    .date("Joining date is required")
    .max(new Date(), "Joining date must be in the past"),
  managerProfileId: z
    .object({ pkUserId: z.string(), email: z.string() })
    .nullable()
    .optional(),
  departmentId: z
    .object({ pkDepartmentId: z.string(), departmentName: z.string() })
    .nullable()
    .refine((v) => v !== null, { message: "Department is required" }),
});

export default function Profile() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      birthDate: undefined,
      joiningDate: undefined,
      managerProfileId: null,
      departmentId: null,
    },
  });

  const { mutate: createProfile, isPending } = useCreateProfile();

  function onSubmit(data: z.infer<typeof formSchema>) {
    createProfile({
      name: data.name,
      birthDate: data.birthDate,
      joiningDate: data.joiningDate,
      departmentId: data.departmentId?.pkDepartmentId,
      managerProfileId: data.managerProfileId?.pkUserId ?? undefined,
    });
    form.reset();
  }

  return (
    <Card className="w-full sm:max-w-xl mx-auto mt-30">
      <CardHeader>
        <CardTitle>Create Profile</CardTitle>
        <CardDescription>
          <div className="w-fit">Fill in the details below to move further</div>
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form
          id="form-rhf-demo"
          onSubmit={form.handleSubmit(onSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-6"
        >
          <FieldGroup>
            <FormField
              form={form}
              name={"name"}
              label={"Name"}
              type="text"
              placeholder={"Name"}
            />
            <DatePicker
              form={form}
              label={"Joining date"}
              name={"joiningDate"}
            />
            <AsyncCombobox
              single={true}
              form={form}
              name={"departmentId"}
              label={"Department"}
              placeholder={"Select department"}
              fetchFunction={useGetDepartments}
              displayKey={"departmentName"}
              primaryKey={"pkDepartmentId"}
            />
          </FieldGroup>
          <FieldGroup>
            <DatePicker
              form={form}
              label={"Date of birth"}
              name={"birthDate"}
            />
            <AsyncCombobox
              single={true}
              form={form}
              name={"managerProfileId"}
              label={"Manager"}
              placeholder={"Select manager"}
              fetchFunction={useUser}
              displayKey={"email"}
              primaryKey={"pkUserId"}
            />
          </FieldGroup>
        </form>
      </CardContent>
      <CardFooter>
        <Field orientation="horizontal">
          <Button type="button" variant="outline" onClick={() => form.reset()}>
            Reset
          </Button>
          <ButtonSpinner
            isPending={isPending}
            form="form-rhf-demo"
            text="Submit"
          />
        </Field>
      </CardFooter>
    </Card>
  );
}
