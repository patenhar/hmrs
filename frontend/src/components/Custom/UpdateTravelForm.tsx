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
import { useCreateTravel, useGetTravelById } from "@/api/queries/useTravel.ts";
import { useNavigate, useParams } from "react-router-dom";
import { AddTravelForm } from "./AddTravelForm";
import { Spinner } from "../ui/spinner";

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

export function UpdateTravelForm() {
  const { travelId } = useParams();
  const { data } = useGetTravelById(travelId);
  console.log(data?.data.data);
  if (!data) return <Spinner />;
  return <AddTravelForm currentData={data?.data.data} isUpdate={true} />;
}
