import { useQuery } from "@tanstack/react-query";
import tagService from "../tagService";

const { getAllTags } = tagService;

export const useGetAllTags = () => {
  return useQuery({
    queryKey: ["Tag"],
    queryFn: () => getAllTags(),
  });
};
