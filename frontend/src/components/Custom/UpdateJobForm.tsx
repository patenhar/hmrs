import { useGetJobById } from "@/api/queries/useJob";
import { useParams } from "react-router-dom";
import { AddJobForm } from "./AddJobForm";
import { Spinner } from "../ui/spinner";

export function UpdateJobForm() {
  const { jobId } = useParams();
  const { data } = useGetJobById(jobId);

  if (!data) return <Spinner />;
  return <AddJobForm currentData={data?.data.data} isUpdate={true} />;
}
