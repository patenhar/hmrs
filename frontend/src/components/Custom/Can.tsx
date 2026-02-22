import { useAuth } from "@/context/AuthContext";
import { hasAuthority } from "@/utils/hasAuthority";
import type React from "react";

interface Props {
  authority: string;
  ownerId?: string;
  children: React.ReactNode;
}
export default function Can({ authority, ownerId, children }: Props) {
  const { user } = useAuth();

  if (!user) return null;

  if (!hasAuthority(user?.authorities, authority)) return null;

  if (ownerId !== undefined && ownerId !== user.pkUserId) {
    return null;
  }

  return <>{children}</>;
}
