import AsyncCombobox from "../Custom/AsyncCombobox";
import { useGetGameByName } from "@/api/queries/useGames";

export default function GameComboboxWrapper({ disabled, form, name }) {
  return (
    <AsyncCombobox
      single={true}
      form={form}
      name={name}
      label={"Game"}
      placeholder={"Select Game"}
      fetchFunction={useGetGameByName}
      displayKey={"gameName"}
      primaryKey={"pkGameId"}
    />
  );
}
