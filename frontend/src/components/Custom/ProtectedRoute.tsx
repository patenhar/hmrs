import { useAuth } from "@/context/AuthContext";
import { Spinner } from "../ui/spinner";
import { Navigate, Outlet } from "react-router-dom";
import { hasAuthority } from "@/utils/hasAuthority";

interface ProtectedRouteProps {
  required?: string | string[];
}

export default function ProtectedRoute({
  required,
}: Readonly<ProtectedRouteProps>) {
  const { user, isLoading, isError } = useAuth();
  const hasToken = !!sessionStorage.getItem("token");

  if (!hasToken) {
    return <Navigate to="/login" replace />;
  }

  if (isLoading || (!user && !isError)) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner className="size-8" />
      </div>
    );
  }

  if (!user) return <Navigate to="/login" replace />;

  if (required && !hasAuthority(user.authorities, required)) {
    return <Navigate to="/unauthorized" replace />;
  }
  return <Outlet />;
}
