import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm, Controller } from "react-hook-form";
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
import {
  FieldGroup,
  Field,
  FieldError,
  FieldLabel,
} from "@/components/ui/field";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Trash } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useGetAllUsers, useUsersByGameAndName } from "@/api/queries/useUser";
import { useNavigate } from "react-router-dom";
import { AsyncMultiCombobox } from "./AsyncMultiCombobox";
import { DatePicker } from "./DatePicker";
import AsyncCombobox from "./AsyncCombobox";
import {
  useBookSlot,
  useGetGameById,
  useGetGameByName,
  useGetGameSlots,
} from "@/api/queries/useGames";
import { toast } from "sonner";

const formSchema = z.object({
  tempGameId: z
    .object({ pkGameId: z.string(), gameName: z.string() })
    .nullable(),
  tempDate: z.date("Date is required"),
  slotId: z.string().min(1, "Slot is required"),
  teamMemberIds: z.array(z.string()).min(1, "At least one user is required"),
});

export function GameBookingForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      tempGameId: null,
      slotId: "",
      teamMemberIds: [],
    },
  });

  const { isPending, mutate: bookSlot } = useBookSlot();
  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log(data);
    bookSlot(
      { gameSlotId: data.slotId, teamMemberIds: data.teamMemberIds },
      {
        onSuccess: () => {
          navigate(-1);
        },
      },
    );
  }

  const { remove: removeUser } = useFieldArray({
    control: form.control,
    name: "teamMemberIds" as never,
  });

  const [searchValue, setSearchValue] = useState("");

  const gameId = form.watch("tempGameId")?.pkGameId ?? "";
  const date = form.watch("tempDate");

  const { isLoading: userLoading, data: userData } = useUsersByGameAndName(
    gameId,
    searchValue,
  );
  const { data: users } = useGetAllUsers();

  const { data: slotData, isLoading: slotLoading } = useGetGameSlots(
    gameId,
    date,
  );

  const { data: gameData, isLoading: gameLoading } = useGetGameById(gameId);

  console.log(slotData);
  const slots: any[] = Array.isArray((slotData as any)?.data?.data)
    ? (slotData as any).data.data
    : [];
  console.log(slots);

  const teamMemberOptions = ((userData as any)?.data?.data ?? []).map(
    (user: any) => ({
      ...user,
      displayName: user?.profile?.name ?? user?.email,
    }),
  );

  useEffect(() => {
    form.setValue("teamMemberIds", []);
    setSearchValue("");
  }, [gameId, form]);

  const navigate = useNavigate();

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) {
          navigate(-1);
        }
      }}
    >
      <DialogContent className="w-full max-w-[90vw] lg:max-w-4xl">
        <DialogHeader>
          <DialogTitle>Book slot</DialogTitle>
          <DialogDescription>
            Create team and book your slot now
          </DialogDescription>
        </DialogHeader>
        <form id="form-rhf-demo" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="no-scrollbar -mx-4 max-h-[50vh] overflow-y-auto px-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FieldGroup className="space-y-4">
                <AsyncCombobox
                  single={true}
                  form={form}
                  name={`tempGameId`}
                  label={"Game"}
                  placeholder={"Select game"}
                  fetchFunction={useGetGameByName}
                  displayKey={"gameName"}
                  primaryKey={"pkGameId"}
                />
                <DatePicker form={form} label={"Date"} name={"tempDate"} />
              </FieldGroup>
              <FieldGroup className="space-y-4">
                <AsyncMultiCombobox
                  disabled={!gameId}
                  prev={form.watch("teamMemberIds")}
                  onValueChange={(value: string[]) => {
                    const unique = [...new Set(value)];
                    if (!gameLoading) {
                      const max =
                        (gameData as any)?.data?.data?.maxPlayers ?? 2;
                      if (unique.length > max) {
                        toast.warning(
                          `Maximum ${max} players are allowed for this game`,
                        );
                        return;
                      }
                    }

                    form.setValue("teamMemberIds", unique);
                  }}
                  form={form}
                  name={"teamMemberIds"}
                  label={"Team members"}
                  placeholder={
                    gameId
                      ? "Search and select team members"
                      : "Select game first"
                  }
                  isLoading={userLoading}
                  queryRes={teamMemberOptions}
                  valueField={"pkUserId"}
                  displayField={"displayName"}
                  onInputChange={setSearchValue}
                />
                <Controller
                  name={"slotId"}
                  control={form.control}
                  render={({ fieldState }) => {
                    const gId = form.watch("tempGameId");
                    const dId = form.watch("tempDate");
                    return (
                      <Field data-invalid={fieldState.invalid}>
                        <FieldLabel htmlFor={"slotId"}>{"Slot"}</FieldLabel>
                        <Select
                          value={form.watch("slotId")}
                          onValueChange={(value) =>
                            form.setValue("slotId", value)
                          }
                          disabled={!gId || !dId || slotLoading}
                        >
                          <SelectTrigger className="w-full max-w-48">
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectGroup>
                              <SelectLabel>Slots</SelectLabel>
                              {slots?.map((item: any) => (
                                <SelectItem
                                  key={item.pkGameSlotId}
                                  value={item.pkGameSlotId}
                                >
                                  {item.beginTime} - {item.endTime}
                                </SelectItem>
                              ))}
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                        {fieldState.invalid && (
                          <FieldError errors={[fieldState.error]} />
                        )}
                      </Field>
                    );
                  }}
                />
              </FieldGroup>
            </div>
            <Table className="mt-8">
              <TableCaption>
                List of selected users for this booking
              </TableCaption>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-81.25">Email</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead className="w-12.5 text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {(form.watch("teamMemberIds") ?? []).map((id, index) => {
                  const user = users?.data?.data?.find(
                    (u: any) => u.pkUserId === id,
                  );
                  return (
                    <TableRow key={user?.pkUserId}>
                      <TableCell className="font-medium">
                        {user?.email ?? ""}
                      </TableCell>
                      <TableCell className="font-medium">
                        {user?.profile?.name ?? ""}
                      </TableCell>

                      <TableCell className="text-right">
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => {
                            removeUser(index);
                          }}
                        >
                          <Trash className="h-4 w-4 text-red-500" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </div>
          <DialogFooter className="col-span-1 md:col-span-2 flex justify-end gap-3">
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isPending}
              form="form-rhf-demo"
              text="Submit"
            />
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
