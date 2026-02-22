import { useState } from "react";
import { AsyncSingleCombobox } from "../Custom/AsyncSingleCombobox";
import { useGetGameByName } from "@/api/queries/useGames";

export default function GameComboboxWrapper({ disabled, form, name }) {
  const [searchValue, setSearchValue] = useState("");

  const { isLoading: GameLoading, data: GameData } =
    useGetGameByName(searchValue);

  console.log(GameData?.data.data);
  return (
    <AsyncSingleCombobox
      form={form}
      name={name}
      label={"Game"}
      placeholder={"Select Game"}
      isLoading={GameLoading}
      queryRes={GameData?.data.data}
      valueField={"pkGameId"}
      displayField={"gameName"}
      onInputChange={setSearchValue}
    />
  );
}
