import { Button } from "@/components/ui/button";
import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from "@/components/ui/empty";
import { File, IndianRupee } from "lucide-react";
import { useNavigate } from "react-router-dom";

export function NoExpense() {
  const navigate = useNavigate();
  return (
    <div className="min-h-[85vh] flex justify-center items-center">
      <div className="w-1/4  h-1/2">
        <Empty className="border border-dashed">
          <EmptyHeader>
            <EmptyMedia variant="icon">
              <IndianRupee />
            </EmptyMedia>
            <EmptyTitle>No Expense</EmptyTitle>
            <EmptyDescription>Add travel expenses.</EmptyDescription>
          </EmptyHeader>
          <EmptyContent>
            <Button variant="default" size="sm" onClick={() => navigate("add")}>
              Fill details
            </Button>
          </EmptyContent>
        </Empty>
      </div>
    </div>
  );
}
