import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
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
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useNavigate } from "react-router-dom";
import { useAddGame, useUpdateGame } from "@/api/queries/useGames";

const formSchema = z
  .object({
    gameName: z.string().min(1, "Game name is required"),
    maxPlayers: z.coerce.number().int().min(1, "Max players cannot be zero"),
    duration: z.coerce.number().min(0.1, "Duration cannot be zero"),
    bookingCycle: z.coerce
      .number()
      .int()
      .min(1, "Booking cycle cannot be zero"),
    operationHourBegin: z
      .string()
      .regex(/^([01]\d|2[0-3]):[0-5]\d$/, "Enter a valid time (HH:mm)"),
    operationHourEnd: z
      .string()
      .regex(/^([01]\d|2[0-3]):[0-5]\d$/, "Enter a valid time (HH:mm)"),
  })
  .refine((d) => d.operationHourEnd > d.operationHourBegin, {
    message: "End time must be after start time",
    path: ["operationHourEnd"],
  });

const toTimeInputValue = (time = "") => time.slice(0, 5);

export function AddGame({ currentData, isUpdate = false }) {
  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: currentData
      ? {
          gameName: currentData.gameName,
          maxPlayers: currentData.maxPlayers,
          duration: currentData.duration,
          bookingCycle: currentData.bookingCycle,
          operationHourBegin: toTimeInputValue(currentData.operationHourBegin),
          operationHourEnd: toTimeInputValue(currentData.operationHourEnd),
        }
      : {
          gameName: "",
          maxPlayers: 1,
          duration: 1,
          bookingCycle: 1,
          operationHourBegin: "",
          operationHourEnd: "",
        },
  });

  const { mutate: addGame, isPending: addPending } = useAddGame();
  const { mutate: updateGame, isPending: updatePending } = useUpdateGame();
  const navigate = useNavigate();
  const isPending = isUpdate ? updatePending : addPending;

  function onSubmit(data) {
    const payload = {
      gameName: data.gameName,
      maxPlayers: data.maxPlayers,
      duration: data.duration,
      bookingCycle: data.bookingCycle,
      operationHourBegin: data.operationHourBegin + ":00",
      operationHourEnd: data.operationHourEnd + ":00",
    };

    if (isUpdate && currentData?.pkGameId) {
      updateGame(
        {
          gameId: currentData.pkGameId,
          data: payload,
        },
        {
          onSuccess: () => navigate(-1),
        },
      );
      return;
    }

    addGame(payload, {
      onSuccess: () => navigate(-1),
    });
  }

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      <form id="add-game-form" onSubmit={form.handleSubmit(onSubmit)}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>{isUpdate ? "Update Game" : "Add Game"}</DialogTitle>
            <DialogDescription>
              Fill in the details below to{" "}
              {isUpdate ? "update the" : "add a new"} game.
            </DialogDescription>
          </DialogHeader>
          <FieldGroup className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="sm:col-span-2">
              <FormField
                form={form}
                name="gameName"
                label="Game Name"
                type="text"
                placeholder="e.g. Table Tennis"
              />
            </div>
            <FormField
              form={form}
              name="maxPlayers"
              label="Max Players"
              type="number"
              placeholder="4"
            />
            <FormField
              form={form}
              name="duration"
              label="Duration (hrs)"
              type="number"
              placeholder="1"
            />
            <FormField
              form={form}
              name="bookingCycle"
              label="Booking Cycle (days)"
              type="number"
              placeholder="7"
            />
            <div />
            <FormField
              form={form}
              name="operationHourBegin"
              label="Opens At"
              type="time"
              placeholder="09:00"
            />
            <FormField
              form={form}
              name="operationHourEnd"
              label="Closes At"
              type="time"
              placeholder="18:00"
            />
          </FieldGroup>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <ButtonSpinner
              isPending={isPending}
              form="add-game-form"
              text={isUpdate ? "Update Game" : "Add Game"}
            />
          </DialogFooter>
        </DialogContent>
      </form>
    </Dialog>
  );
}
