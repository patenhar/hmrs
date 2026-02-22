import axiosClient from "./axiosClient";

const gameService = {
  getGameByName: (name: string) =>
    axiosClient.get("/games/search", { params: { name } }),
  getGameById: (id: string) => axiosClient.get(`/games/${id}`),
  getGameByUser: (userId: string) =>
    axiosClient.get(`/game/bookings/users/${userId}`),
  getGameSlots: (gameId, date) =>
    axiosClient.get("game/bookings/slots/available", {
      params: { gameId, date },
    }),
  bookSlot: (bookingData) => axiosClient.post("game/bookings/", bookingData),
};

export default gameService;
