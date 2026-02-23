import { useAuth } from "@/context/AuthContext";
import { hasAuthority } from "@/utils/hasAuthority";
import type React from "react";

interface Props {
  authority: string;
  ownerId?: string;
  extraAuthority?: string;
  children: React.ReactNode;
}
export default function Can({
  authority,
  ownerId,
  extraAuthority,
  children,
}: Props) {
  const { user } = useAuth();
  const authorities = user?.authorities || [];

  if (!hasAuthority(authorities, authority)) return null;
  if (!ownerId && !extraAuthority) return <>{children}</>;
  if (extraAuthority && hasAuthority(authorities, extraAuthority))
    return <>{children}</>;
  if (ownerId && user?.pkUserId === ownerId) return <>{children}</>;

  return null;

  // if (!user) return null;

  // if (!hasAuthority(user?.authorities, authority)) return null;

  // if (extraAuthority && !hasAuthority(user?.authorities, extraAuthority)) return null;

  // if (ownerId !== undefined && ownerId !== user.pkUserId) {
  //   return null;
  // }

  // return <>{children}</>;
}
