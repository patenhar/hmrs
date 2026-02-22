import { useGetTravelById, useGetTravelUsers } from "@/api/queries/useTravel";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardAction,
  CardFooter,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useNavigate, useParams } from "react-router-dom";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Upload } from "lucide-react";
import { AddTravelForm } from "@/components/Custom/AddTravelForm";
import ButtonLink from "@/components/Custom/ButtonLink";
import { Button } from "@/components/ui/button";
import Can from "@/components/Custom/Can";

export default function TravelInfo() {
  const { travelId } = useParams();

  const { isLoading, data } = useGetTravelById(travelId);
  const { isLoading: travelUsersLoading, data: travelUserData } =
    useGetTravelUsers(travelId);
  const navigate = useNavigate();

  var travel = {};
  var travelUsers = [];
  if (!travelUsersLoading) {
    travel = data?.data.data;
    console.log(travelUserData?.data.data);
    travelUsers = travelUserData?.data.data;
  }
  return (
    <main className="flex justify-center items-center min-h-[90vh] w-full">
      <Card className="w-[55vw]">
        <CardHeader>
          <CardTitle>
            <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
              {travel.title}
            </h4>
          </CardTitle>
          <CardDescription>{travel.description}</CardDescription>
          <CardAction>
            <Button
              variant={"default"}
              onClick={() => {
                navigate("update");
              }}
            >
              Update travel
            </Button>
          </CardAction>
        </CardHeader>
        <Separator />
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center gap-1 w-full">
              <p className="text-muted-foreground text-sm">Travel Date:</p>
              <p className="leading-7">{travel.travelDate}</p>
            </div>
            <div className="flex items-center gap-1 w-full">
              <p className="text-muted-foreground text-sm">Return Date:</p>
              <p className="leading-7">{travel.returnDate}</p>
            </div>
            <div className="flex items-center gap-1 w-full">
              <p className="text-muted-foreground text-sm">HR Mail:</p>
              <p className="leading-7">{travel.hrMail}</p>
            </div>
            <div className="flex items-center gap-1 w-full">
              <p className="text-muted-foreground text-sm">Grant Limit:</p>
              <p className="leading-7">{travel.maxGrantPerDay}</p>
            </div>
          </div>
          <div className="flex flex-col items-center gap-1 w-full mt-4">
            <Table>
              <TableHeader className="bg-gray-50">
                <TableRow>
                  <TableHead>Sr.</TableHead>
                  <TableHead>Address</TableHead>
                  <TableHead>City</TableHead>
                  <TableHead>Country</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {travel.destinations?.map((d, idx) => (
                  <TableRow key={d.pkAddressId}>
                    <TableCell>{idx + 1}</TableCell>
                    <TableCell>
                      {d.addressLine1}, {d.addressLine2}
                    </TableCell>
                    <TableCell>{d.city.cityName}</TableCell>
                    <TableCell>{d.city.country.countryName}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
          <div className="flex flex-col items-center gap-1 w-full mt-4">
            <Table>
              <TableHeader className="bg-gray-50">
                <TableRow>
                  <TableHead>Sr.</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead></TableHead>
                  <TableHead></TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {travelUsers.userTravels?.map((tu, idx) => (
                  <TableRow key={tu.pkUserTravelId} className="cursor-pointer">
                    <TableCell>{idx + 1}</TableCell>
                    <TableCell>{tu.user.profile?.name}</TableCell>
                    <TableCell>{tu.user.email}</TableCell>
                    <Can authority="VIEW_EXPENSE" ownerId={tu.user.pkUserId}>
                      <TableCell>
                        <ButtonLink
                          to={`/travels/users/${tu.pkUserTravelId}/expenses`}
                          text="Expenses"
                        />
                      </TableCell>
                    </Can>
                    <Can authority="VIEW_DOCUMENT" ownerId={tu.user.pkUserId}>
                      <TableCell>
                        <ButtonLink
                          to={`/travels/users/${tu.pkUserTravelId}/documents`}
                          text="Documents"
                        />
                      </TableCell>
                    </Can>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </main>
  );
}
