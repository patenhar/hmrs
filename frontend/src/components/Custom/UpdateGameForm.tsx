import { useGetGameById } from "@/api/queries/useGames";
import { useParams } from "react-router-dom";
import { AddGame } from "./AddGame.jsx";
import { Spinner } from "../ui/spinner";

export function UpdateGameForm() {
  const { gameId } = useParams();
  const { data } = useGetGameById(gameId);

  if (!data) return <Spinner />;
  return <AddGame currentData={data?.data.data} isUpdate={true} />;
}
