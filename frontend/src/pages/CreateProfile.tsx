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
import { Field, FieldGroup, FieldLabel } from "@/components/ui/field";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner.tsx";
import FormField from "@/components/Custom/FormField.tsx";
import { DatePicker } from "@/components/Custom/DatePicker.tsx";
import { useEffect } from "react";
import { useUser } from "@/api/queries/useUser.tsx";
import AsyncCombobox from "@/components/Custom/AsyncCombobox.tsx";
import { useGetDepartments } from "@/api/queries/useDepartment.tsx";
import {
  useCreateProfile,
  useGetProfileByUserId,
  useUpdateProfile,
} from "@/api/queries/useProfile.tsx";
import { useNavigate, useParams } from "react-router-dom";
import { useGetAllGames } from "@/api/queries/useGames.tsx";
import { Checkbox } from "@/components/ui/checkbox";
import { Spinner } from "@/components/ui/spinner";

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
    .nullable(),
  gameIds: z.array(z.string()).optional(),
});

type CreateProfileProps = {
  isEdit?: boolean;
};

type GameOption = {
  pkGameId: string;
  gameName: string;
};

export default function CreateProfile({
  isEdit = false,
}: Readonly<CreateProfileProps>) {
  const { userId } = useParams();
  const navigate = useNavigate();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      birthDate: undefined,
      joiningDate: undefined,
      managerProfileId: null,
      departmentId: null,
      gameIds: [],
    },
  });

  const { mutate: createProfile, isPending: creating } = useCreateProfile();
  const { mutate: updateProfile, isPending: updating } = useUpdateProfile();

  const { data: currentProfileData, isLoading: profileLoading } =
    useGetProfileByUserId(userId ?? "", isEdit);

  const currentProfile = currentProfileData?.data?.data;

  useEffect(() => {
    if (!isEdit || !currentProfile) return;
    form.reset({
      name: currentProfile.name ?? "",
      birthDate: currentProfile.birthDate
        ? new Date(currentProfile.birthDate + "T00:00:00")
        : undefined,
      joiningDate: currentProfile.joiningDate
        ? new Date(currentProfile.joiningDate + "T00:00:00")
        : undefined,
      managerProfileId: currentProfile.managerProfile?.user
        ? {
            pkUserId: currentProfile.managerProfile.user.pkUserId,
            email: currentProfile.managerProfile.user.email ?? "",
          }
        : null,
      departmentId: currentProfile.department
        ? {
            pkDepartmentId: currentProfile.department.pkDepartmentId,
            departmentName: currentProfile.department.departmentName ?? "",
          }
        : null,
      gameIds: (currentProfile.games ?? []).map(
        (game: GameOption) => game.pkGameId,
      ),
    });
  }, [currentProfile, form, isEdit]);

  const isPending = creating || updating;

  function formatLocalDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  function onSubmit(data: z.infer<typeof formSchema>) {
    const payload = {
      ...data,
      birthDate: formatLocalDate(data.birthDate),
      joiningDate: formatLocalDate(data.joiningDate),
      departmentId: data.departmentId?.pkDepartmentId,
      managerProfileId: data.managerProfileId?.pkUserId ?? undefined,
    };
    if (isEdit && currentProfile?.pkProfileId) {
      updateProfile(
        { profileId: currentProfile.pkProfileId, data: payload },
        {
          onSuccess: () => navigate(`/profiles/${currentProfile?.pkProfileId}`),
        },
      );
      return;
    }

    createProfile({ ...payload, userId });
  }

  const { data: gameData } = useGetAllGames();

  const games: GameOption[] = gameData?.data?.data ?? [];
  const selectedGameIds = form.watch("gameIds") ?? [];

  if (isEdit && profileLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner className="size-8" />
      </div>
    );
  }

  return (
    <Card className="w-full sm:max-w-xl mx-auto mt-30">
      <CardHeader>
        <CardTitle>{isEdit ? "Edit Profile" : "Create Profile"}</CardTitle>
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
            <Field>
              <FieldLabel htmlFor={"gameIds"}>Game interests</FieldLabel>
              <div className="space-y-3 rounded-md border p-3">
                <div className="flex gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={() =>
                      form.setValue(
                        "gameIds",
                        games.map((game) => game.pkGameId),
                      )
                    }
                  >
                    Select all
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={() => form.setValue("gameIds", [])}
                  >
                    Clear all
                  </Button>
                </div>
                <div className="max-h-40 space-y-2 overflow-y-auto pr-1">
                  {games.map((game) => {
                    const checked = selectedGameIds.includes(game.pkGameId);
                    return (
                      <label
                        key={game.pkGameId}
                        className="flex items-center gap-2 text-sm"
                      >
                        <Checkbox
                          checked={checked}
                          onCheckedChange={(value) => {
                            const updated = value
                              ? [...selectedGameIds, game.pkGameId]
                              : selectedGameIds.filter(
                                  (id) => id !== game.pkGameId,
                                );
                            form.setValue("gameIds", updated);
                          }}
                        />
                        {game.gameName}
                      </label>
                    );
                  })}
                </div>
              </div>
            </Field>
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
            text={isEdit ? "Update" : "Submit"}
          />
        </Field>
      </CardFooter>
    </Card>
  );
}
