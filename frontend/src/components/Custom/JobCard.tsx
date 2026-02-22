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
import { ShareJobForm } from "./ShareJobForm";
import { ReferJobForm } from "./ReferJobForm";
import { useNavigate } from "react-router-dom";

export function JobCard({ jobId, title, description, accessUrl }) {
  const navigate = useNavigate();
  return (
    <Card size="sm" className="w-full max-w-sm" onClick={() => navigate(jobId)}>
      <CardHeader>
        <CardTitle>{title}</CardTitle>
        <CardDescription>
          <ButtonLink to={accessUrl} text="View job description" />
        </CardDescription>
      </CardHeader>
      <CardContent>
        <p>{description}</p>
      </CardContent>
      <CardFooter>
        <div className="flex gap-4">
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
        </div>
      </CardFooter>
    </Card>
  );
}
