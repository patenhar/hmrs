import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import notificationService from "../notificationService";

const { getNotifications, markAsRead } = notificationService;

export const useGetNotifications = () => {
  return useQuery({
    queryKey: ["Notifications"],
    queryFn: () => getNotifications().then((res) => res.data.data),
  });
};

export const useMarkAsRead = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => markAsRead(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["Notifications"] });
    },
    onError: (error: Error) => {
      toast.error("Failed to mark notification as read", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
