import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { toast } from "sonner";
import travelService from "../travelService.ts";

// const queryClient = useQueryClient();

const {
  getAllTravels,
  getTravelById,
  createTravel,
  updateTravel,
  deleteTravel,
  getTravelUsers,
  getUserTravelDocuments,
  uploadTravelDocument,
  deleteTravelDocument,
} = travelService;

export const useGetAllTravel = () => {
  return useQuery({
    queryKey: ["Travel"],
    queryFn: () => getAllTravels(),
  });
};

export const useGetTravelById = (id: string) => {
  return useQuery({
    queryKey: ["Travel", id],
    queryFn: () => getTravelById(id),
  });
};

export const useCreateTravel = () => {
  return useMutation({
    mutationFn: createTravel,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Travel creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useUpdateTravel = () => {
  return useMutation({
    mutationFn: updateTravel,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Travel creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useDeleteTravel = () => {
  return useMutation({
    mutationFn: deleteTravel,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Travel creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useGetTravelUsers = (id: string) => {
  return useQuery({
    queryKey: ["Travel", id],
    queryFn: () => getTravelUsers(id),
  });
};

export const useGetUserTravelDocuments = (id: string) => {
  return useQuery({
    queryKey: ["TravelDoc", id],
    queryFn: () => getUserTravelDocuments(id),
  });
};

export const useUploadTravelDocument = (userTravelId) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: uploadTravelDocument,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["TravelDoc", userTravelId] });
    },
    onError: (error) => {
      toast.error("Travel creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useDeleteTravelDocument = (userTravelId) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteTravelDocument,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["TravelDoc", userTravelId] });
    },
    onError: (error) => {
      toast.error("Travel creation failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
