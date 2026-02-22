import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
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
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  FieldGroup,
  Field,
  FieldError,
  FieldLabel,
} from "@/components/ui/field";
import { Controller } from "react-hook-form";

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
import { Plus, Trash } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { AsyncSingleCombobox } from "./AsyncSingleCombobox";
import { GetUserById, useUser } from "@/api/queries/useUser";
import { DatePickerWithRange } from "./DatePickerWithRange";
import { useRegister } from "@/api/queries/useAuth";
import CityComboboxWrapper from "../wrappers/CityComboboxWrapper";
import CountryComboboxWrapper from "../wrappers/CountryComboboxWrapper";
import { useCreateTravel, useUpdateTravel } from "@/api/queries/useTravel.ts";
import { useNavigate } from "react-router-dom";
import { AsyncMultiCombobox } from "./AsyncMultiCombobox";
import { DatePicker } from "./DatePicker";
import GameComboboxWrapper from "../wrappers/GameCombobxWrapper";
import {
  useBookSlot,
  useGetGameById,
  useGetGameSlots,
} from "@/api/queries/useGames";
import { toast } from "sonner";

const formSchema = z.object({
  tempGameId: z.string().min(1, "Game is required"),
  tempDate: z.date("Date is required"),
  slotId: z.string().min(1, "Slot is required"),
  teamMemberIds: z.array(z.string()).min(1, "At least one user is required"),
});

export function GameBookingForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      tempGameId: "",
      slotId: "",
      teamMemberIds: [],
    },
  });

  const { isPending, mutate: bookSlot } = useBookSlot();
  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log(data);
    bookSlot({ gameSlotId: data.slotId, teamMemberIds: data.teamMemberIds });
  }

  const {
    fields,
    append: appendUser,
    remove: removeUser,
  } = useFieldArray({
    control: form.control,
    name: "teamMemberIds",
  });

  const [searchValue, setSearchValue] = useState("");

  const { isLoading: userLoading, data: userData } = useUser(searchValue);
  const { isLoading, data: users } = useUser("");

  const gameId = form.watch("tempGameId");
  const date = form.watch("tempDate");

  const { data: slotData, isLoading: slotLoading } = useGetGameSlots(
    gameId,
    date,
  );

  const { data: gameData, isLoading: gameLoading } = useGetGameById(gameId);

  console.log(slotData);
  const slots = Array.isArray(slotData?.data.data) ? slotData.data.data : [];
  console.log(slots);

  const navigate = useNavigate();

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
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
                <GameComboboxWrapper
                  disabled={false}
                  form={form}
                  name={`tempGameId`}
                />
                <DatePicker form={form} label={"Date"} name={"tempDate"} />
              </FieldGroup>
              <FieldGroup className="space-y-4">
                <AsyncMultiCombobox
                  disabled={false}
                  prev={form.watch("teamMemberIds")}
                  onValueChange={(value) => {
                    const unique = [...new Set(value)] ?? [];
                    if (!gameLoading) {
                      const max = gameData?.data.data.maxPlayers ?? 2;
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
                  placeholder={"Select team members"}
                  isLoading={userLoading}
                  queryRes={userData?.data.data}
                  valueField={"pkUserId"}
                  displayField={"email"}
                  onInputChange={setSearchValue}
                />
                <Controller
                  name={"slotId"}
                  control={form.control}
                  render={({ field, fieldState }) => {
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
                              {slots?.map((item) => (
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
                List of selected users for this travel
              </TableCaption>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[325px]">Email</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead className="w-[50px] text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {(form.watch("teamMemberIds") ?? []).map((id, index) => {
                  const user = users?.data?.data?.find(
                    (u) => u.pkUserId === id,
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
            <ButtonSpinner isPending={isPending} text="Submit" />
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
