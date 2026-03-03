import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import ButtonLink from "./ButtonLink";
import { useNavigate } from "react-router-dom";
import { useDeleteJob } from "@/api/queries/useJob";
import Can from "@/components/Custom/Can";
import { Trash } from "lucide-react";

export function JobCard({ jobId, title, description, accessUrl }) {
  const navigate = useNavigate();
  const { mutate: deleteJob } = useDeleteJob();
  return (
    <Card size="sm" className="w-full" onClick={() => navigate(jobId)}>
      <CardHeader>
        <CardTitle className="line-clamp-2">{title}</CardTitle>
        <CardDescription>
          <ButtonLink to={accessUrl} text="View job description" />
        </CardDescription>
      </CardHeader>
      <CardContent>
        <p className="line-clamp-3 text-sm text-muted-foreground">
          {description}
        </p>
      </CardContent>
      <CardFooter>
        <div className="flex gap-4 w-full">
          <Button
            variant="secondary"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`${jobId}/share`);
            }}
          >
            Share Job
          </Button>

          <Button
            variant="default"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`${jobId}/refer`);
            }}
          >
            Refer Job
          </Button>

          <Can authority="MANAGE_ALL_JOB">
            <Button
              variant="ghost"
              size="icon"
              className="ml-auto"
              onClick={(e) => {
                e.stopPropagation();
                deleteJob(jobId);
              }}
            >
              <Trash className="h-4 w-4 text-red-500" />
            </Button>
          </Can>
        </div>
      </CardFooter>
    </Card>
  );
}
