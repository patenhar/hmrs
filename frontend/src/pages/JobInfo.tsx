import { useGetJobById } from "@/api/queries/useJob";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardAction,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useNavigate, useParams } from "react-router-dom";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import ButtonLink from "@/components/Custom/ButtonLink";
import Can from "@/components/Custom/Can";

export default function JobInfo() {
  const { jobId } = useParams();

  const { isLoading, data } = useGetJobById(jobId);
  console.log(jobId);
  const navigate = useNavigate();

  var Job = {};
  if (!isLoading) {
    Job = data?.data.data;
    console.log(Job);
  }
  return (
    <main className="flex justify-center items-center min-h-[90vh] w-full">
      <Card className="w-[55vw]">
        <CardHeader>
          <CardTitle>
            <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
              {Job.title}
            </h4>
          </CardTitle>
          <CardDescription>{Job.description}</CardDescription>
          <CardAction>
            <Can authority={"MANAGE_ALL_JOB"}>
              <Button
                variant={"default"}
                onClick={() => {
                  navigate("update");
                }}
              >
                Update Job
              </Button>
            </Can>
          </CardAction>
        </CardHeader>
        <Separator />
        <CardContent>
          <div className="flex items-center gap-1 w-full">
            <p className="text-muted-foreground text-sm">HR Mail:</p>
            <p className="leading-7">
              <ButtonLink
                to={Job?.jd?.accessUrl}
                text={
                  Job?.jd?.accessUrl
                    ? new URL(Job?.jd?.accessUrl).pathname.split("/").pop()
                    : "No Access URL"
                }
              />
            </p>
          </div>
          <div className="flex flex-col items-center gap-1 w-full mt-4">
            <Table>
              <TableHeader className="bg-gray-50">
                <TableRow>
                  <TableHead>Sr.</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead>Type</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {Job.jobStakeHolders?.map((tu, idx) => (
                  <TableRow
                    key={tu.jobStakeHolderId}
                    className="cursor-pointer"
                  >
                    <TableCell>{idx + 1}</TableCell>
                    <TableCell>{tu.user?.profile?.name}</TableCell>
                    <TableCell>{tu.user?.email}</TableCell>
                    <TableCell>{tu.jobStakeHolderType}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </main>
  );
}
