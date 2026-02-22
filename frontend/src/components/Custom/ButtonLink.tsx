import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

export default function ButtonLink({ to, text }: { to: string; text: string }) {
  return (
    <Button variant={"link"} size={"xs"} className="p-0">
      <Link to={to}>{text}</Link>
    </Button>
  );
}
