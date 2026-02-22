import {
  useDeleteTravelDocument,
  useGetUserTravelDocuments,
} from "@/api/queries/useTravel";
import ButtonLink from "@/components/Custom/ButtonLink";
import { NoDocument } from "@/components/Custom/NoDocument";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Trash } from "lucide-react";
import { Outlet, useParams } from "react-router-dom";

export default function UserTravelDocuments() {
  const { userTravelId } = useParams();
  const { isLoading, data } = useGetUserTravelDocuments(userTravelId);
  const { isPending, mutate: deleteData } =
    useDeleteTravelDocument(userTravelId);
  var documents = [];
  if (!isLoading) {
    documents = data?.data.data;
  }
  return documents.length == 0 ? (
    <NoDocument />
  ) : (
    <div className="p-8">
      <Table>
        <TableHeader className="bg-gray-50">
          <TableRow>
            <TableHead>Sr.</TableHead>
            <TableHead>File</TableHead>
            <TableHead>Document type</TableHead>
            <TableHead>Uploaded by</TableHead>
            <TableHead>Uploaded at</TableHead>
            <TableHead className="w-[50px] text-right">Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {documents.map((d, idx) => (
            <TableRow key={d.pkTravelDocumentId} className="cursor-pointer">
              <TableCell>{idx + 1}</TableCell>
              <TableCell>
                <ButtonLink
                  to={d.document.accessUrl}
                  text={new URL(d.document.accessUrl).pathname.split("/").pop()}
                />
              </TableCell>
              <TableCell>{d.document.documentType.documentTypeName}</TableCell>
              <TableCell>{d.uploadedBy.email}</TableCell>
              <TableCell>{d.createAt}</TableCell>
              <Button
                variant="ghost"
                size="icon"
                className="p-0 flex justify-end"
                onClick={() => {
                  deleteData(d.pkTravelDocumentId);
                }}
              >
                <Trash className="h-4 w-4 text-red-500" />
              </Button>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Outlet />
    </div>
  );
}
