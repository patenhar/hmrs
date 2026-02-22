import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";

export function ButtonSpinner({ isPending, form, text }) {
  return isPending ? (
    <Button type="submit" variant="secondary" disabled>
      <Spinner data-icon="inline-start" />
      {text}
    </Button>
  ) : (
    <Button type="submit" form={form}>
      {text}
    </Button>
  );
}
