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
}: Readonly<Props>) {
  const { user } = useAuth();
  const authorities = user?.authorities || [];

  const hasPrimary = hasAuthority(authorities, authority);
  const hasExtra =
    !!extraAuthority && hasAuthority(authorities, extraAuthority);

  if (!hasPrimary && !hasExtra) return null;
  if (hasExtra) return <>{children}</>;
  if (hasPrimary && !ownerId) return <>{children}</>;
  if (hasPrimary && ownerId && user?.pkUserId === ownerId)
    return <>{children}</>;

  return null;

  // if (!user) return null;

  // if (!hasAuthority(user?.authorities, authority)) return null;

  // if (extraAuthority && !hasAuthority(user?.authorities, extraAuthority)) return null;

  // if (ownerId !== undefined && ownerId !== user.pkUserId) {
  //   return null;
  // }

  // return <>{children}</>;
}
