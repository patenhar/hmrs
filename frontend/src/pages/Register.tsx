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
import RoleComboboxWrapper from "@/components/wrappers/RoleComboboxWrapper.tsx";
import { is, ro } from "date-fns/locale";
import { useNavigate } from "react-router";

const formSchema = z
  .object({
    email: z.email("Invalid email address"),
    roleId: z.string().min(1, "Role is required"),
    password: z.string().min(6, "Password must be at least 6 characters"),
    confirmPassword: z
      .string()
      .min(6, "Confirm Password must be at least 6 characters"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords do not match",
    path: ["confirmPassword"],
  });

export default function Register() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
      confirmPassword: "",
      roleId: "",
    },
  });

  const { mutateAsync: register, isPending } = useRegister();
  const navigate = useNavigate();
  async function onSubmit(data: z.infer<typeof formSchema>) {
    const res = await register(data);
    console.log(res);
    navigate(`/users/${res.data.data.pkUserId}/profile/create`);
  }

  return (
    <Card className="w-full sm:max-w-md mx-auto mt-10">
      <CardHeader>
        <CardTitle>Register</CardTitle>
        <CardDescription>
          <div className="w-fit">
            Already have an account?
            <ButtonLink to="/login" text="Login now" />
          </div>
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form id="form-rhf-demo" onSubmit={form.handleSubmit(onSubmit)}>
          <FieldGroup>
            <FormField
              form={form}
              name="email"
              label="Email"
              type="text"
              placeholder="something@roimaint.com"
            />
            <FormField
              form={form}
              name="password"
              label="Password"
              type="password"
              placeholder="Password"
            />
            <FormField
              form={form}
              name="confirmPassword"
              label="Confirm Password"
              type="password"
              placeholder="Confirm Password"
            />
            <RoleComboboxWrapper disabled={false} form={form} name="roleId" />
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
