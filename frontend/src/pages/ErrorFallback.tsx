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

export function ErrorFallback({ error }) {
  const navigate = useNavigate();
  return (
    <div className="min-h-[85vh] flex justify-center items-center">
      <div className="w-1/4  h-1/2">
        <Empty className="border border-dashed">
          <EmptyHeader>
            <EmptyMedia variant="icon">
              <X />
            </EmptyMedia>
            <EmptyTitle>Error</EmptyTitle>
            <div className="w-full">Something went wrong!</div>
          </EmptyHeader>
          <EmptyContent>
            <EmptyDescription className="text-red-400">
              {error.message}
            </EmptyDescription>
            <EmptyDescription>
              There might be an issue from our side. Report the issue if you
              feel like.
            </EmptyDescription>
          </EmptyContent>
        </Empty>
      </div>
    </div>
  );
}
