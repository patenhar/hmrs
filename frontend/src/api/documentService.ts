import axiosClient from "./axiosClient";

const documentService = {
  getDocumentTypes: (query: string) =>
    axiosClient.get("/document-types/search", { params: { query } }),
};

export default documentService;
