import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
import * as z from "zod";
import type { MouseEvent } from "react";

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
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useRef } from "react";
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
import { useUser } from "@/api/queries/useUser";
import { DatePickerWithRange } from "./DatePickerWithRange";
import CityComboboxWrapper from "../wrappers/CityComboboxWrapper";
import { useGetCountryByName } from "@/api/queries/useCountry";
import { useCreateTravel, useUpdateTravel } from "@/api/queries/useTravel.ts";
import { useNavigate } from "react-router-dom";
import AsyncCombobox from "./AsyncCombobox";

const getFormSchema = (isUpdate: boolean) =>
  z.object({
    title: z.string().min(1, "Title is required"),
    description: z.string().min(1, "Description is required"),
    maxGrantPerDay: z.coerce.number().min(1, "Grant cannot be zero"),
    hrMail: z.object({
      email: z.email("Invalid email address"),
    }),
    userIds: z
      .array(
        z.object({
          pkUserId: z.string().min(1, "Invalid user ID"),
        }),
      )
      .min(1, "At least one user is required"),
    destinations: z
      .array(
        z.object({
          addressLine1: z.string().min(1, "Address line 1 is required"),
          addressLine2: z.string().min(1, "Address line 2 is required"),
          countryId: z
            .object({ pkCountryId: z.string(), countryName: z.string() })
            .nullable(),
          cityId: z
            .object({ pkCityId: z.string(), cityName: z.string() })
            .nullable(),
        }),
      )
      .min(1, "At least one destination is required"),
    dateRange: z.object({
      from: isUpdate
        ? z.date()
        : z.date().min(new Date(), "Travel date must be in the future"),
      to: isUpdate
        ? z.date()
        : z.date().min(new Date(), "Return date must be in the future"),
    }),
  });

type props = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  currentData?: any;
  isUpdate?: boolean;
};

export function AddTravelForm({
  currentData,
  isUpdate = false,
}: Readonly<props>) {
  const formSchema = getFormSchema(isUpdate);
  const form = useForm<z.infer<typeof formSchema>>({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    resolver: zodResolver(formSchema) as any,
    defaultValues: currentData
      ? {
          title: currentData.title,
          description: currentData.description,
          maxGrantPerDay: currentData.maxGrantPerDay,
          hrMail: currentData.hrMail,
          destinations:
            currentData.destinations?.map((d: any) => ({
              addressLine1: d.addressLine1,
              addressLine2: d.addressLine2,
              cityId: d.city
                ? { pkCityId: d.city.pkCityId, cityName: d.city.cityName }
                : null,
              countryId: d.city?.country
                ? {
                    pkCountryId: d.city.country.pkCountryId,
                    countryName: d.city.country.countryName,
                  }
                : null,
            })) ?? [],
          userIds: currentData.userTravels.map((ut: any) => ({
            pkUserId: ut.user.pkUserId,
            email: ut.user.email,
            profile: ut.user.profile,
          })),
          dateRange: {
            from: currentData.travelDate
              ? new Date(currentData.travelDate)
              : undefined,
            to: currentData.returnDate
              ? new Date(currentData.returnDate)
              : undefined,
          },
        }
      : {
          title: "",
          description: "",
          maxGrantPerDay: 0,
          hrMail: "",
          destinations: [
            {
              addressLine1: "",
              addressLine2: "",
              cityId: null,
              countryId: null,
            },
          ],
          userIds: [],
          dateRange: {
            from: undefined,
            to: undefined,
          },
        },
  });

  const { mutate: createTravel, isPending: addPending } = useCreateTravel();
  const { mutate: updateTravel, isPending: updatePending } = useUpdateTravel();

  const isPending = isUpdate ? updatePending : addPending;

  function onSubmit(data: z.infer<typeof formSchema>) {
    console.log(data);
    const payload = {
      title: data.title,
      description: data.description,
      maxGrantPerDay: data.maxGrantPerDay,
      hrMail: data.hrMail.email,
      destinations: data.destinations.map((d) => {
        return {
          addressLine1: d.addressLine1,
          addressLine2: d.addressLine2,
          cityId: d.cityId?.pkCityId,
        };
      }),
      userIds: data.userIds.map((u) => u.pkUserId),
      travelDate: data.dateRange.from,
      returnDate: data.dateRange.to,
    };
    console.log(payload);
    if (isUpdate && currentData?.pkTravelId) {
      updateTravel(
        { travelId: currentData.pkTravelId, ...payload },
        {
          onSuccess: () => {
            navigate(-1);
          },
        },
      );
    } else {
      createTravel(payload, {
        onSuccess: () => {
          navigate(-1);
        },
      });
    }
  }

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "destinations",
  });

  const { remove: removeUser } = useFieldArray({
    control: form.control,
    name: "userIds",
  });

  const addAddress = (e: MouseEvent) => {
    e.preventDefault();
    append({
      addressLine1: "",
      addressLine2: "",
      countryId: null,
      cityId: null,
    });
  };

  const selectedValuesRef = useRef<{ updateSelectedValues: () => void } | null>(
    null,
  );
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
      <DialogContent className="w-full max-w-[90vw] lg:max-w-3xl">
        <DialogHeader>
          <DialogTitle>Add Travel</DialogTitle>
          <DialogDescription>
            Fill in the below details to add new travel
          </DialogDescription>
        </DialogHeader>
        <form id="form-rhf-demo" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="no-scrollbar -mx-4 max-h-[50vh] overflow-y-auto px-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FormField
                form={form}
                name={"title"}
                label={"Title"}
                type="text"
                placeholder={"Title"}
              />
              <FormField
                form={form}
                name={"description"}
                label={"Description"}
                type="text"
                placeholder={"Description"}
              />
              <FormField
                form={form}
                name={"maxGrantPerDay"}
                label={"Maximum Grant Per Day"}
                type="number"
                placeholder={"10000"}
              />
              <DatePickerWithRange
                form={form}
                label={"Date range"}
                name={"dateRange"}
              />
              <AsyncCombobox
                single={true}
                form={form}
                name={"hrMail"}
                label={"HR Mail"}
                placeholder={"Select HR mail for this travel"}
                fetchFunction={useUser}
                displayKey={"email"}
                primaryKey={"pkUserId"}
              />
              {/* <NewAsyncSingleCombobox
                  disabled={false}
                  form={form}
                  name={"hrMail"}
                  label={"HR Mail"}
                  placeholder={"Select HR mail for this travel"}
                  fetchFunction={useUser}
                  displayKey={"email"}
                  primaryKey={"pkUserId"}
                /> */}

              <AsyncCombobox
                single={false}
                form={form}
                name={"userIds"}
                label={"Users"}
                placeholder={"Select users for this travel"}
                fetchFunction={useUser}
                ref={selectedValuesRef}
                displayKey={"email"}
                primaryKey={"pkUserId"}
              />

              {/* <NewAsyncMultiCombobox
                  disabled={false}
                  form={form}
                  name={"userIds"}
                  label={"Users"}
                  placeholder={"Select users for this travel"}
                  fetchFunction={useUser}
                  ref={selectedValuesRef}
                  displayKey={"email"}
                  primaryKey={"pkUserId"}
                /> */}
            </div>
            {fields.map((des, idx) => (
              <div
                className="col-span-2 flex gap-4 mt-11 items-end"
                key={des.id}
              >
                <FormField
                  form={form}
                  name={`destinations.${idx}.addressLine1`}
                  label={"Address line 1"}
                  type="text"
                  placeholder={"Address line 1"}
                />
                <FormField
                  form={form}
                  name={`destinations.${idx}.addressLine2`}
                  label={"Address line 2"}
                  type="text"
                  placeholder={"Address line 2"}
                />
                <AsyncCombobox
                  single={true}
                  form={form}
                  name={`destinations.${idx}.countryId`}
                  label={"Country"}
                  placeholder={"Select country"}
                  fetchFunction={useGetCountryByName}
                  displayKey={"countryName"}
                  primaryKey={"pkCountryId"}
                />
                <CityComboboxWrapper
                  disabled={false}
                  form={form}
                  idx={idx}
                  name={`destinations.${idx}.cityId`}
                />
                {idx === form.getValues("destinations").length - 1 ? (
                  <Button variant={"outline"} onClick={addAddress}>
                    <Plus />
                  </Button>
                ) : (
                  <Button
                    variant="destructive"
                    onClick={(e) => {
                      e.preventDefault();
                      remove(idx);
                    }}
                  >
                    <Trash />
                  </Button>
                )}
              </div>
            ))}

            <Table className="mt-8">
              <TableCaption>
                List of selected users for this travel
              </TableCaption>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-81.25">Email</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead className="w-12.5 text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {form.watch("userIds")?.map((user, index) => {
                  if (!user) return null;
                  const u = user as unknown as {
                    profile?: { name?: string };
                    email?: string;
                  };
                  return (
                    <TableRow key={user.pkUserId}>
                      <TableCell className="font-medium">
                        {u.profile?.name}
                      </TableCell>
                      <TableCell className="font-medium">{u.email}</TableCell>
                      <TableCell className="text-right">
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => {
                            removeUser(index);
                            selectedValuesRef.current?.updateSelectedValues();
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
