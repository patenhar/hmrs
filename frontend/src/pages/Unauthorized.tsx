import { Button } from "@/components/ui/button";
import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from "@/components/ui/empty";
import { X } from "lucide-react";
import { useNavigate } from "react-router-dom";

export function Unauthorized() {
  const navigate = useNavigate();
  return (
    <div className="min-h-[85vh] flex justify-center items-center">
      <div className="w-[40vw]  h-1/2">
        <Empty className="border border-dashed">
          <EmptyHeader>
            <EmptyMedia variant="icon">
              <X />
            </EmptyMedia>
            <EmptyTitle>OOPS!</EmptyTitle>
            <div className="w-full">
              You are not authorized to access this resource.
            </div>
          </EmptyHeader>
          <EmptyContent>
            <EmptyDescription>
              Try signing in or contact person in-charge.
            </EmptyDescription>
            <Button
              variant="default"
              size="sm"
              onClick={() => navigate("/zlogin")}
            >
              Login
            </Button>
          </EmptyContent>
        </Empty>
      </div>
    </div>
  );
}
