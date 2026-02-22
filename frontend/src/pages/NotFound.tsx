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

export function NotFound() {
  const navigate = useNavigate();
  return (
    <div className="min-h-[85vh] flex justify-center items-center">
      <div className="w-1/4  h-1/2">
        <Empty className="border border-dashed">
          <EmptyHeader>
            <EmptyMedia variant="icon">
              <X />
            </EmptyMedia>
            <EmptyTitle>404</EmptyTitle>
            <div className="w-full">Not Found!</div>
          </EmptyHeader>
          <EmptyContent>
            <EmptyDescription>You are accessing wrong page.</EmptyDescription>
            <Button variant="default" size="sm" onClick={() => navigate("/")}>
              Go back home
            </Button>
          </EmptyContent>
        </Empty>
      </div>
    </div>
  );
}
