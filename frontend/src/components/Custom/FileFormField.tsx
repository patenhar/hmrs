import { Controller } from "react-hook-form";
import { Input } from "@/components/ui/input";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";

export default function FileFormField({ form, name, label, placeholder }) {
  return (
    <Controller
      name={name}
      control={form.control}
      render={({ field: { value, onChange, ...field }, fieldState }) => (
        <Field data-invalid={fieldState.invalid}>
          <FieldLabel htmlFor={name}>{label}</FieldLabel>
          <Input
            {...field}
            id={name}
            type="file"
            aria-invalid={fieldState.invalid}
            value={value?.fileName}
            onChange={(event) => {
              onChange(event.target.files[0]);
            }}
            accept=".pdf, .docx"
            placeholder={placeholder}
          />
          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
        </Field>
      )}
    />
  );
}
