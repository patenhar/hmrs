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
import { FieldGroup } from "@/components/ui/field";
import FormField from "@/components/Custom/FormField";
import { ButtonSpinner } from "@/components/Custom/ButtonSpinner";
import { useState } from "react";
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
import { AsyncSingleCombobox } from "./AsyncSingleCombobox";
import { GetUserById, useUser } from "@/api/queries/useUser";
import { DatePickerWithRange } from "./DatePickerWithRange";
import { useRegister } from "@/api/queries/useAuth";
import CityComboboxWrapper from "../wrappers/CityComboboxWrapper";
import CountryComboboxWrapper from "../wrappers/CountryComboboxWrapper";
import { useCreateTravel, useUpdateTravel } from "@/api/queries/useTravel.ts";
import { useNavigate } from "react-router-dom";
import { AsyncMultiCombobox } from "./AsyncMultiCombobox";

const formSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  maxGrantPerDay: z.coerce.number().min(1, "Grant cannot be zero"),
  hrMail: z.email("Invalid email format"),
  userIds: z.array(z.string()).min(1, "At least one user is required"),
  destinations: z
    .array(
      z.object({
        addressLine1: z.string().min(1, "Address line 1 is required"),
        addressLine2: z.string().min(1, "Address line 2 is required"),
        countryId: z.string().min(1, "Country is required"),
        cityId: z.string().min(1, "City is required"),
      }),
    )
    .min(1, "At least one destination is required"),
  dateRange: z.object({
    from: z
      .date("Travel date is required")
      .min(new Date(), "Travel date must be in the future"),
    to: z
      .date("Return date is required")
      .min(new Date(), "Return date must be in the future"),
  }),
  // .refine(
  //   (object) => {
  //     return !object.from || !object.to ? false : true;
  //   },
  //   { message: "Date range is required", path: ["dateRange"] },
  // ),
});

type props = {
  currentData?: any;
  isUpdate?: boolean;
};

export function AddTravelForm({ currentData, isUpdate = false }: props) {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: currentData
      ? {
          title: currentData.title,
          description: currentData.description,
          maxGrantPerDay: currentData.maxGrantPerDay,
          hrMail: currentData.hrMail,
          destinations: currentData.destinations,
          userIds: currentData.userTravels.map((ut) => ut.user.pkUserId),
          dateRange: {
            from: currentData.travelDate,
            to: currentData.returnDate,
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
              cityId: "",
              countryId: "",
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

  var isPending = false;
  function onSubmit(data: z.infer<typeof formSchema>) {
    const payload = {
      title: data.title,
      description: data.description,
      maxGrantPerDay: data.maxGrantPerDay,
      hrMail: data.hrMail,
      destinations: data.destinations.map((d) => {
        return {
          addressLine1: d.addressLine1,
          addressLine2: d.addressLine2,
          cityId: d.cityId,
        };
      }),
      userIds: data.userIds,
      travelDate: data.dateRange.from,
      returnDate: data.dateRange.to,
    };
    if (isUpdate && currentData?.pkTravelId) {
      isPending = updatePending;

      updateTravel({ travelId: currentData.pkTravelId, ...payload });
    } else {
      isPending = addPending;

      createTravel(payload);
    }
  }

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "destinations",
  });

  const { append: appendUser, remove: removeUser } = useFieldArray({
    control: form.control,
    name: "userIds",
  });

  const addAddress = (e) => {
    event?.preventDefault();
    append({ addressLine1: "", addressLine2: "", countryId: "", cityId: "" });
  };

  const [searchValue, setSearchValue] = useState("");

  const { isLoading: userLoading, data: userData } = useUser(searchValue);

  const users = userData?.data?.data ?? [];
  const userIds = form.watch("userIds") ?? [];

  const navigate = useNavigate();

  return (
    <Dialog
      open={true}
      onOpenChange={(open) => {
        if (!open) navigate(-1);
      }}
    >
      {/* <DialogTrigger asChild>
        <Button variant="default">Add Travel</Button>
      </DialogTrigger> */}
      <DialogContent className="w-full max-w-[90vw] lg:max-w-4xl">
        <DialogHeader>
          <DialogTitle>Add Travel</DialogTitle>
          <DialogDescription>
            Fill in the below details to add new travel
          </DialogDescription>
        </DialogHeader>
        <form id="form-rhf-demo" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="no-scrollbar -mx-4 max-h-[50vh] overflow-y-auto px-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FieldGroup className="space-y-4">
                <FormField
                  form={form}
                  name={"title"}
                  label={"Title"}
                  type="text"
                  placeholder={"Title"}
                />
                <FormField
                  form={form}
                  name={"hrMail"}
                  label={"HR Mail"}
                  type="email"
                  placeholder={"something@roimaint.com"}
                />
                <DatePickerWithRange
                  form={form}
                  label={"Date range"}
                  name={"dateRange"}
                />
              </FieldGroup>
              <FieldGroup className="space-y-4">
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
                <AsyncMultiCombobox
                  disabled={false}
                  prev={form.watch("userIds")}
                  onValueChange={appendUser}
                  form={form}
                  name={"userIds"}
                  label={"Users"}
                  placeholder={"Select travel users"}
                  isLoading={userLoading}
                  queryRes={users}
                  valueField={"pkUserId"}
                  displayField={"email"}
                  onInputChange={setSearchValue}
                />
              </FieldGroup>
            </div>
            {fields.map((des, idx) => (
              <div className="col-span-2 flex gap-4 mt-11 items-end" key={idx}>
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
                <CountryComboboxWrapper
                  disabled={false}
                  form={form}
                  name={`destinations.${idx}.countryId`}
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
                    onClick={() => {
                      event?.preventDefault();
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
                  <TableHead className="w-[325px]">Email</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead className="w-[50px] text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {userIds.map((id, index) => {
                  const user = users?.find((u) => u.pkUserId === id);
                  if (!user) return null;
                  return (
                    <TableRow key={user.pkUserId}>
                      <TableCell className="font-medium">
                        {user.profile?.name}
                      </TableCell>
                      <TableCell className="font-medium">
                        {user.email}
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
