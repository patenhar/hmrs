import { useGetAllJobs } from "@/api/queries/useJob";
import { useNavigate } from "react-router-dom";
import { Spinner } from "@/components/ui/spinner";
import { JobCard } from "@/components/Custom/JobCard";
import { Button } from "@/components/ui/button";
import Can from "@/components/Custom/Can";

export function Job() {
  const { isLoading, data } = useGetAllJobs();
  const navigate = useNavigate();
  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center h-64">
          <Spinner className="size-8"/>
        </div>
      ) : (
        <div className="p-8">
          <div className="flex items-center justify-between mb-4">
            <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
              Jobs
            </h4>
            <Can authority={"ADD_JOB"}>
              <Button onClick={() => navigate("add")}>Add Job</Button>
            </Can>
          </div>
          <div className="p-1 flex gap-4 overflow-auto">
            {data?.data.data.map((job) => (
              <JobCard
                key={job.pkJobId}
                jobId={job.pkJobId}
                title={job.title}
                description={job.description}
                accessUrl={job.jd.accessUrl}
              />
            ))}
          </div>
        </div>
      )}
    </>
  );
}
