import {
  useMutation,
  useQuery,
  useQueryClient,
  keepPreviousData,
} from "@tanstack/react-query";
import GameService from "../gameService.tsx";
import type { GamePageParams, GameBookingPageParams } from "../gameService.tsx";

import { format } from "date-fns";
import { toast } from "sonner";

const {
  getGameByName,
  getGameById,
  getGameSlots,
  bookSlot,
  getGameByUser,
  addGame,
  updateGame,
  getAllGames,
  getGamesPaginated,
  getGameBookingsPaginated,
} = GameService;

export const useGetAllGames = () => {
  return useQuery({
    queryKey: ["Games"],
    queryFn: () => getAllGames(),
  });
};

export const useGetGamesPaginated = (params: GamePageParams) => {
  return useQuery({
    queryKey: ["Games", "paginated", params],
    queryFn: () => getGamesPaginated(params),
    placeholderData: keepPreviousData,
  });
};

export const useGetGameBookingsPaginated = (params: GameBookingPageParams) => {
  return useQuery({
    queryKey: ["GameBookings", "paginated", params],
    queryFn: () => getGameBookingsPaginated(params),
    placeholderData: keepPreviousData,
    enabled: !!params["user-id"],
  });
};

export const useGetGameByName = (name: string) => {
  return useQuery({
    queryKey: ["Game", name],
    queryFn: () => getGameByName(name),
    enabled: name.length > 0,
  });
};

export const useGetGameById = (id: string) => {
  return useQuery({
    queryKey: ["Game", id],
    queryFn: () => getGameById(id),
    enabled: !!id,
  });
};

export const useGetGameByUser = (id: string) => {
  return useQuery({
    queryKey: ["Game", id],
    queryFn: () => getGameByUser(id),
    enabled: !!id,
  });
};

export const useGetGameSlots = (gameId, date) => {
  return useQuery({
    queryKey: ["Game", gameId],
    queryFn: () => {
      if (!gameId || !date) return [];
      const formattedDate = format(date, "yyyy-MM-dd");
      return getGameSlots(gameId, formattedDate);
    },
    enabled: !!gameId && !!date,
  });
};

export const useBookSlot = () => {
  return useMutation({
    mutationFn: bookSlot,
    onSuccess: (res) => {
      toast.success(res.data.message);
    },
    onError: (error) => {
      toast.error("Booking failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useAddGame = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: addGame,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Games"] });
    },
    onError: (error) => {
      toast.error("Failed to add game", {
        description: error.message || "Something went wrong",
      });
    },
  });
};

export const useUpdateGame = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updateGame,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Games"] });
    },
    onError: (error) => {
      toast.error("Failed to update game", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
