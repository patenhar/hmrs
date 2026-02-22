import {
  useApproveExpense,
  useGetUserTravelExpenses,
  useRejectExpense,
} from "@/api/queries/useExpense";
import { useGetGameByUser } from "@/api/queries/useGames";
import ButtonLink from "@/components/Custom/ButtonLink";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Check, X } from "lucide-react";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { Outlet, useNavigate, useParams } from "react-router-dom";

export default function Expense() {
  const { userId } = useParams();
  const { isLoading, data } = useGetGameByUser(
    "62941e53-eb42-4fb2-8baa-711bfc752b53",
  );
  // const { isPending, mutate: deleteData } =
  //   useDeleteTravelDocument(userTravelId);
  var games = [];
  if (!isLoading) {
    games = data?.data.data;
    console.log(games);
  }
  const navigate = useNavigate();
  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Game Bookings
        </h4>
        <Button onClick={() => navigate("add")}>Book Slot</Button>
      </div>
      <Table>
        <TableHeader className="bg-gray-50">
          <TableRow>
            <TableHead>Sr.</TableHead>
            <TableHead>Game</TableHead>
            <TableHead>Date</TableHead>
            <TableHead>Begin time</TableHead>
            <TableHead>End time</TableHead>
            <TableHead>Booked by</TableHead>
            <TableHead>Status</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {games.map((e, idx) => (
            <Tooltip key={e.pkGameBookingId}>
              <TooltipTrigger asChild>
                <TableRow key={e.pkGameBookingId} className="cursor-pointer">
                  <TableCell>{idx + 1}</TableCell>
                  <TableCell>{e.gameSlot.game.gameName}</TableCell>
                  <TableCell>{e.gameSlot.date}</TableCell>
                  <TableCell>{e.gameSlot.beginTime}</TableCell>
                  <TableCell>{e.gameSlot.endTime}</TableCell>
                  <TableCell>{e.bookedBy.email}</TableCell>
                  <TableCell>
                    {e.gameBookingStatus.gameBookingStatusName}
                  </TableCell>
                </TableRow>
              </TooltipTrigger>
              <TooltipContent>
                <ul className="my-6 ml-6 list-disc [&>li]:mt-2">
                  {e.teamMembers.map((m) => (
                    <li key={m.pkUserId}>{m.email}</li>
                  ))}
                </ul>
              </TooltipContent>
            </Tooltip>
          ))}
        </TableBody>
      </Table>
      <Outlet />
    </div>
  );
}
