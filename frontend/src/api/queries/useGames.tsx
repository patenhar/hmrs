import { useMutation, useQuery } from "@tanstack/react-query";
import GameService from "../gameService.tsx";

import { format } from "date-fns";
import { toast } from "sonner";

const { getGameByName, getGameById, getGameSlots, bookSlot, getGameByUser } =
  GameService;

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
