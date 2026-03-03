import axiosClient from "./axiosClient";

export interface GamePageParams {
  page: number;
  size: number;
  sort: string;
  "game-name"?: string;
  "max-players-min"?: number;
  "max-players-max"?: number;
}

export interface GameBookingPageParams {
  page: number;
  size: number;
  sort: string;
  "user-id"?: string;
  "game-name"?: string;
  "status-name"?: string;
  "date-from"?: string;
  "date-to"?: string;
}

const gameService = {
  getAllGames: () => axiosClient.get("/games/"),
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
  addGame: (gameData) => axiosClient.post("/games/", gameData),
  updateGame: ({ gameId, data }) => axiosClient.patch(`/games/${gameId}`, data),
  getGamesPaginated: (params: GamePageParams) =>
    axiosClient.get("/games/filtering&pagination&sorting", { params }),
  getGameBookingsPaginated: (params: GameBookingPageParams) =>
    axiosClient.get("/game/bookings/filtering&pagination&sorting", { params }),
};

export default gameService;
