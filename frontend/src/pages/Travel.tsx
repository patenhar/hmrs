import { useDeleteTravel, useGetAllTravel } from "@/api/queries/useTravel.ts";
import { AddTravelForm } from "@/components/Custom/AddTravelForm";
import Can from "@/components/Custom/Can";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Trash } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Travel() {
  const { isLoading, data } = useGetAllTravel();
  const { isPending, mutate: deleteTravel } = useDeleteTravel();
  const navigate = useNavigate();
  return isLoading ? (
    <Spinner />
  ) : (
    <div className="p-8">
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Travels
        </h4>
        <Can authority={"ADD_JOB"}>
          <Button onClick={() => navigate("add")}>Add Travel</Button>
        </Can>
      </div>

      <Table>
        <TableCaption>A list of travel plans.</TableCaption>
        <TableHeader className="bg-gray-50">
          <TableRow>
            <TableHead className="w-[100px]">Title</TableHead>
            <TableHead>Description</TableHead>
            <TableHead>Travel date</TableHead>
            <TableHead>Return date</TableHead>
            <TableHead>Grant limit</TableHead>
            <TableHead>HR mail</TableHead>
            <TableHead></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data?.data.data.map((travel) => (
            <TableRow
              key={travel.pkTravelId}
              onClick={() => navigate(travel.pkTravelId)}
              className="cursor-pointer"
            >
              <TableCell className="font-medium">{travel.title}</TableCell>
              <TableCell>{travel.description}</TableCell>
              <TableCell>{travel.travelDate}</TableCell>
              <TableCell>{travel.returnDate}</TableCell>
              <TableCell>{travel.maxGrantPerDay}</TableCell>
              <TableCell>{travel.hrMail}</TableCell>
              <TableCell>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => {
                    event?.stopPropagation();
                    deleteTravel(travel.pkTravelId);
                  }}
                >
                  <Trash className="h-4 w-4 text-red-500" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
