import axiosClient from "./axiosClient";

type auth = { email: string; password: string };

const authService = {
  register: (registerData: auth) =>
    axiosClient.post("/auth/register", registerData),
  login: (loginData: auth) => {
    console.log(loginData);
    return axiosClient.post(`/auth/login`, loginData);
  },
};

export default authService;
