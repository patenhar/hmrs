import { useGetCurrentUser } from "@/api/queries/useUser";
import { createContext, useContext } from "react";

export const AuthContext = createContext({
  user: null,
  loading: true,
  error: false,
});

export const AuthProvider = ({ children }) => {
  const { data, isLoading, isError } = useGetCurrentUser();
  return (
    <AuthContext.Provider
      value={{
        user: data?.data.data ?? null,
        isLoading,
        isError,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
