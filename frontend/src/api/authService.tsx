import axiosClient from "./axiosClient";

type auth = { email: string; password: string };

const productServices = {
  register: (registerData: auth) =>
    axiosClient.post("/auth/register", registerData),
  login: (loginData: auth) => {
    console.log(loginData);
    return axiosClient.post(`/auth/login`, loginData);
  },
};

export default productServices;
