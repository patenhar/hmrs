import { useMutation, useQuery } from "@tanstack/react-query";
import jobService from "../jobService.tsx";
import { toast } from "sonner";

const { getAllJobs, getJobById, addJob, shareJob, referJob } = jobService;

export const useGetAllJobs = () => {
  return useQuery({
    queryKey: ["Jobs"],
    queryFn: () => getAllJobs(),
  });
};

export const useGetJobById = (id) => {
  return useQuery({
    queryKey: ["Jobs", id],
    queryFn: () => getJobById(id),
  });
};

export const useAddJob = () => {
  return useMutation({
    mutationFn: addJob,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Job adding failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useShareJob = () => {
  return useMutation({
    mutationFn: shareJob,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Job adding failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useReferJob = () => {
  return useMutation({
    mutationFn: referJob,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Job adding failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
