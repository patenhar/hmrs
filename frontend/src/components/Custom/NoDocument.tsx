import { Button } from "@/components/ui/button";
import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from "@/components/ui/empty";
import { File } from "lucide-react";
import { useNavigate } from "react-router-dom";

export function NoDocument() {
  const navigate = useNavigate();
  return (
    <div className="min-h-[85vh] flex justify-center items-center">
      <div className="w-1/4  h-1/2">
        <Empty className="border border-dashed">
          <EmptyHeader>
            <EmptyMedia variant="icon">
              <File />
            </EmptyMedia>
            <EmptyTitle>No Travel Document</EmptyTitle>
            <EmptyDescription>Upload travel documents.</EmptyDescription>
          </EmptyHeader>
          <EmptyContent>
            <Button
              variant="default"
              size="sm"
              onClick={() => navigate("upload")}
            >
              Upload Files
            </Button>
          </EmptyContent>
        </Empty>
      </div>
    </div>
  );
}
