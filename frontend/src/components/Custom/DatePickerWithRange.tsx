import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import { Field, FieldLabel, FieldError } from "@/components/ui/field";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { format } from "date-fns";
import { CalendarIcon } from "lucide-react";
import { Controller } from "react-hook-form";

export function DatePickerWithRange({ form, label, name }) {
  return (
    <Controller
      name={name}
      control={form.control}
      render={({ field, fieldState }) => (
        <Field className="mx-auto w-full" data-invalid={fieldState.invalid}>
          <FieldLabel htmlFor="date-picker-range">{label}</FieldLabel>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                id={name}
                className="justify-start px-2.5 font-normal"
              >
                <CalendarIcon />
                {field.value?.from ? (
                  field.value.to ? (
                    <>
                      {format(field.value.from, "LLL dd, y")} -{" "}
                      {format(field.value.to, "LLL dd, y")}
                    </>
                  ) : (
                    format(field.value.from, "LLL dd, y")
                  )
                ) : (
                  <span>Pick a date</span>
                )}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                {...field}
                mode="range"
                defaultMonth={field.value?.from}
                selected={field.value}
                numberOfMonths={2}
                disabled={(date) => date <= new Date()}
                onSelect={field.onChange}
              />
            </PopoverContent>
          </Popover>
          {fieldState.invalid && (
            <FieldError errors={[fieldState.error.from]} />
          )}
        </Field>
      )}
    />
  );
}
