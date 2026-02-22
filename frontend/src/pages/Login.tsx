import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { useEffect } from "react";

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
import ButtonLink from "@/components/Custom/ButtonLink";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useLogin } from "@/api/queries/useAuth";
import { useNavigate } from "react-router-dom";
import FormField from "@/components/Custom/FormField";

const formSchema = z.object({
  email: z.email("Invalid email address"),
  password: z.string().min(1, "Password is required"),
});

export default function Login() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const { mutate: login, isPending, isSuccess } = useLogin();
  const navigate = useNavigate();

  useEffect(() => {
    if (isSuccess) navigate("/travels");
  }, [isSuccess]);

  function onSubmit(data: z.infer<typeof formSchema>) {
    login(data);
  }

  return (
    <Card className="w-full sm:max-w-md mx-auto mt-30">
      <CardHeader>
        <CardTitle>Login</CardTitle>
        <CardDescription>
          <div className="w-fit">
            New here?
            <ButtonLink to="/register" text="Register now" />
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
