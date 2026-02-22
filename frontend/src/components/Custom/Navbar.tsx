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
import { Bell } from "lucide-react";

export default function Navbar() {
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
                <DropdownMenuItem>
                  <Link to="jobs/add">Add Job</Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
              <DropdownMenuGroup>
                <DropdownMenuSeparator />
                <DropdownMenuItem>
                  <Link to="referrals">View Referrals</Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
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
                <DropdownMenuItem>
                  <Link to="travels/add">Add Travel</Link>
                </DropdownMenuItem>
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
        </div>
        <div className="ml-auto flex items-center gap-4">
          <Bell className="text-sm w-4 h-4 font-semibold" />
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">Profile</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Profile</DropdownMenuLabel>
                <DropdownMenuItem>
                  <Link to="profile">View Profile</Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Link to="profile/org-chart">View OrgChar</Link>
                </DropdownMenuItem>
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
    </nav>
  );
}
