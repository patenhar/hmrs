import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";
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
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { useRegister } from "../api/queries/useAuth.tsx";
import ButtonLink from "@/components/Custom/ButtonLink.tsx";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner.tsx";
import FormField from "@/components/Custom/FormField.tsx";
import { DatePicker } from "@/components/Custom/DatePicker.tsx";
import { useState } from "react";
import { useUser } from "@/api/queries/useUser.tsx";
import { AsyncSingleCombobox } from "@/components/Custom/AsyncSingleCombobox.tsx";
import { useJobStakeHolderTypes } from "@/api/queries/useJobStakeHolders.tsx";
import { useGetDepartments } from "@/api/queries/useDepartment.tsx";
import { useCreateProfile } from "@/api/queries/useProfile.tsx";
import { useParams } from "react-router-dom";

const formSchema = z.object({
  name: z.string().min(1, "Name is required"),
  birthDate: z
    .date("Birth date is required")
    .max(new Date(), "Birth date must be in the past"),
  joiningDate: z
    .date("Joining date is required")
    .max(new Date(), "Joining date must be in the past"),
  managerProfileId: z.string().optional(),
  departmentId: z.string("Department is required"),
});

export default function CreateProfile() {
  const { userId } = useParams();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      birthDate: undefined,
      joiningDate: undefined,
      managerProfileId: "",
      departmentId: null,
    },
  });

  const { mutate: createProfile, isPending } = useCreateProfile();

  function onSubmit(data: z.infer<typeof formSchema>) {
    createProfile({ ...data, userId });
    form.reset();
  }

  const [searchValue, setSearchValue] = useState("");
  const { isLoading: managerLoading, data: managerData } = useUser(searchValue);
  const { isLoading: departmentLoading, data: departmentData } =
    useGetDepartments(searchValue);

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
            <AsyncSingleCombobox
              form={form}
              name={"departmentId"}
              label={"Department"}
              placeholder={"Select department"}
              isLoading={departmentLoading}
              queryRes={departmentData?.data.data}
              valueField={"pkDepartmentId"}
              displayField={"departmentName"}
              onInputChange={setSearchValue}
            />
          </FieldGroup>
          <FieldGroup>
            <DatePicker
              form={form}
              label={"Date of birth"}
              name={"birthDate"}
            />
            <AsyncSingleCombobox
              form={form}
              name={"managerProfileId"}
              label={"Manager"}
              placeholder={"Select manager"}
              isLoading={managerLoading}
              queryRes={managerData?.data.data}
              valueField={"pkUserId"}
              displayField={"email"}
              onInputChange={setSearchValue}
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
