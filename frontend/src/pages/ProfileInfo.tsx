import { useGetProfileById } from "@/api/queries/useProfile";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardAction,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useNavigate, useParams } from "react-router-dom";
import ButtonLink from "@/components/Custom/ButtonLink";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";

export default function ProfileInfo() {
  const { profileId } = useParams();
  const { isLoading, isError, data } = useGetProfileById(profileId!);
  const navigate = useNavigate();

  if (isLoading) {
    return (
      <main className="flex justify-center items-center min-h-[90vh] w-full">
        <Spinner className="size-8" />
      </main>
    );
  }

  if (isError || !data?.data?.data) {
    return (
      <main className="flex justify-center items-center min-h-[90vh] w-full">
        <p className="text-muted-foreground">Failed to load profile.</p>
      </main>
    );
  }

  const profile = data.data.data;
  console.log(profile);

  return (
    <main className="flex justify-center items-center min-h-[90vh] w-full">
      <Card className="w-[55vw]">
        <CardHeader>
          <CardTitle>
            <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
              {profile?.name}
            </h4>
          </CardTitle>
          <CardDescription>{profile?.user?.email}</CardDescription>
          <CardAction>
            <div className="flex items-center gap-4">
              <ButtonLink
                to={`/profile/${profileId}/org-chart`}
                text="View Org Chart"
              />
              <Button
                variant="default"
                onClick={() =>
                  navigate(`/users/${profile?.user?.pkUserId}/profile`)
                }
              >
                Edit Profile
              </Button>
            </div>
          </CardAction>
        </CardHeader>
        <Separator />
        <CardContent className="pt-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Name:</p>
              <p className="leading-7">{profile?.name}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Email:</p>
              <p className="leading-7">{profile?.user?.email}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Date of Birth:</p>
              <p className="leading-7">{profile?.birthDate}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Joining Date:</p>
              <p className="leading-7">{profile?.joiningDate}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Department:</p>
              <p className="leading-7">{profile?.department?.departmentName}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Role:</p>
              <p className="leading-7">{profile?.user?.role?.roleName}</p>
            </div>
            <div className="flex items-center gap-1">
              <p className="text-muted-foreground text-sm">Status:</p>
              <p className="leading-7">{profile?.profileStatus}</p>
            </div>
            <div className="flex items-center gap-1 md:col-span-2">
              <p className="text-muted-foreground text-sm">Game Interests:</p>
              <p className="leading-7">
                {profile?.games?.length
                  ? profile.games.map((game) => game.gameName).join(", ")
                  : "None"}
              </p>
            </div>
            {profile?.managerProfile && (
              <div className="flex items-center gap-1">
                <p className="text-muted-foreground text-sm">Manager:</p>
                <ButtonLink
                  to={`/profiles/${profile.managerProfile.pkProfileId}`}
                  text={profile.managerProfile.name}
                />
              </div>
            )}
          </div>
        </CardContent>
      </Card>
    </main>
  );
}
