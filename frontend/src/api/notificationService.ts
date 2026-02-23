import axiosClient from "./axiosClient";

const notificationService = {
  getNotifications: () => axiosClient.get("/users/notifications"),
  markAsRead: (id: string) =>
    axiosClient.patch(`/users/notifications/${id}/read`),
};

export default notificationService;
