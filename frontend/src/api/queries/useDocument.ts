import { useQuery } from "@tanstack/react-query";
import documentService from "../documentService.ts";

const { getDocumentTypes } = documentService;

export const useGetDocumentTypes = (name: string) => {
  return useQuery({
    queryKey: ["DocumentTypes", name],
    queryFn: () => getDocumentTypes(name),
  });
};
