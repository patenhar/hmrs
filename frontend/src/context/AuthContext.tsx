import { useGetCurrentUser } from "@/api/queries/useUser";
import { createContext, useContext, useMemo, useState } from "react";

type AuthUser = {
  pkUserId?: string;
  authorities?: string[];
  profile?: {
    pkProfileId?: string;
  };
};

type AuthContextType = {
  user: AuthUser | null;
  isLoading: boolean;
  isError: boolean;
  setToken: (token: string | null) => void;
};

export const AuthContext = createContext<AuthContextType>({
  user: null,
  isLoading: true,
  isError: false,
  setToken: () => {},
});

export const AuthProvider = ({ children }) => {
  const [token, setTokenState] = useState<string | null>(() =>
    sessionStorage.getItem("token"),
  );

  const setToken = (newToken: string | null) => {
    if (newToken) {
      sessionStorage.setItem("token", newToken);
    } else {
      sessionStorage.removeItem("token");
    }
    setTokenState(newToken);
  };

  const { data, isPending, isFetching, isError } = useGetCurrentUser();
  const value = useMemo(
    () => ({
      user: data?.data.data ?? null,
      isLoading: isFetching || (!!token && isPending),
      isError,
      setToken,
    }),
    [data, isPending, isFetching, isError, token],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
