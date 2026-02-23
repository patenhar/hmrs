import { useAuth } from "@/context/authContext";
import React from "react";
import { Spinner } from "../ui/spinner";
import { Outlet, useNavigate } from "react-router-dom";
import { hasAuthority } from "@/utils/hasAuthority";

export default function ProtectedRoute({ required }) {
  const navigate = useNavigate();
  const { user, isLoading } = useAuth();
  if (isLoading)
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner className="size-8" />
      </div>
    );
  if (!user) return navigate("/login");
  if (required && !hasAuthority(user.authorities, required)) {
    return navigate("/unauthorized");
  }
  return <Outlet />;
}
