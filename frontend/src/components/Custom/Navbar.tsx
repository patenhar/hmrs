import { Link } from "react-router-dom";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "../ui/button";
import { Bell, LogOut } from "lucide-react";
import { useLogout } from "@/api/queries/useAuth";
import {
  useGetNotifications,
  useMarkAsRead,
} from "@/api/queries/useNotification";
import { useAuth } from "@/context/AuthContext";
import Can from "./Can";

type NotificationItem = {
  pkNotificationId: string;
  title: string;
  description: string;
  read: boolean;
};

export default function Navbar() {
  const { data: notifications = [] } = useGetNotifications();
  const { mutate: markAsRead } = useMarkAsRead();
  const unreadCount = notifications.filter(
    (n: { read: boolean }) => !n.read,
  ).length;
  const { user } = useAuth();
  const profileId =
    user && typeof user === "object" && "profile" in user
      ? (user as { profile?: { pkProfileId?: string } }).profile?.pkProfileId
      : undefined;
  const logout = useLogout();
  const notificationItems: NotificationItem[] = Array.isArray(notifications)
    ? (notifications as NotificationItem[])
    : [];

  return (
    <nav className="border-bg bg-gray-50 px-4 py-2">
      <div className="flex items-center justify-right">
        <h4 className="text-md font-semibold">HRMS</h4>
        <div className="ml-15 flex gap-4">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Jobs</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Jobs</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to="jobs">View Jobs</Link>
                </DropdownMenuItem>
                <Can authority={"MANAGE_ALL_JOB"}>
                  <DropdownMenuItem>
                    <Link to="jobs/add">Add Job</Link>
                  </DropdownMenuItem>
                </Can>
              </DropdownMenuGroup>
              <Can authority={"MANAGE_ALL_REFERRAL"}>
                <DropdownMenuGroup>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem>
                    <Link to="referrals">View Referrals</Link>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </Can>
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Travels</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Travels</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to="travels">View Travels</Link>
                </DropdownMenuItem>
                <Can authority={"MANAGE_ALL_TRAVEL"}>
                  <DropdownMenuItem>
                    <Link to="travels/add">Add Travel</Link>
                  </DropdownMenuItem>
                </Can>
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Games</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Games</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to="games">View Games</Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Link to="games/bookings">View Bookings</Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Link to="games/bookings/add">Book Slot</Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Social</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Social</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to="social/posts">View Posts</Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Link to="social/posts/add">Create Post</Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
          <Can authority={"MANAGE_ALL_USER"}>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost">Users</Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuGroup>
                  <DropdownMenuLabel>Users</DropdownMenuLabel>
                  <DropdownMenuItem>
                    <Link to="users">View Users</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    <Link to="roles-permissions">Roles & Permissions</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    <Link to="register">Add User</Link>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </Can>
        </div>
        <div className="ml-auto flex items-center gap-4">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="relative p-2">
                <Bell className="w-4 h-4" />
                {unreadCount > 0 && (
                  <span className="absolute -top-1 -right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] text-white font-bold"></span>
                )}
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-80">
              <DropdownMenuLabel className="font-semibold">
                Notifications
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              {notifications.length === 0 ? (
                <p className="px-4 py-3 text-sm text-muted-foreground">
                  No notifications
                </p>
              ) : (
                notificationItems.map((notification) => (
                  <DropdownMenuItem
                    key={notification.pkNotificationId}
                    className={`flex flex-col items-start gap-1 px-4 py-3 cursor-pointer ${
                      notification.read ? "" : "bg-blue-50"
                    }`}
                    onClick={() =>
                      !notification.read &&
                      markAsRead(notification.pkNotificationId)
                    }
                  >
                    <div className="flex w-full items-center justify-between">
                      <span className="text-sm font-medium">
                        {notification.title}
                      </span>
                      {!notification.read && (
                        <span className="h-2 w-2 rounded-full bg-blue-500 shrink-0" />
                      )}
                    </div>
                    <span className="text-xs text-muted-foreground line-clamp-2">
                      {notification.description}
                    </span>
                  </DropdownMenuItem>
                ))
              )}
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Profile</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Profile</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to={profileId ? `/profiles/${profileId}` : "#"}>
                    View Profile
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Link
                    to={profileId ? `/profile/${profileId}/org-chart` : "#"}
                  >
                    View Org Chart
                  </Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                onClick={logout}
                className="text-red-600 cursor-pointer focus:text-red-600"
              >
                <LogOut className="mr-2 h-4 w-4" />
                Logout
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
    </nav>
  );
}
