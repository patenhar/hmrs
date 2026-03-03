import { useMemo } from "react";

const DOCUMENT_TYPES = [
  { pkDocumentTypeId: "PASSPORT", documentTypeName: "Passport" },
  { pkDocumentTypeId: "VISA", documentTypeName: "Visa" },
  { pkDocumentTypeId: "FLIGHT_TICKET", documentTypeName: "Flight Ticket" },
  { pkDocumentTypeId: "HOTEL_BOOKING", documentTypeName: "Hotel Booking" },
  {
    pkDocumentTypeId: "TRAVEL_INSURANCE",
    documentTypeName: "Travel Insurance",
  },
  { pkDocumentTypeId: "JOB_DESCRIPTION", documentTypeName: "Job Description" },
  { pkDocumentTypeId: "OTHER", documentTypeName: "Other" },
];

export const useGetDocumentTypes = (search: string) => {
  const q = (search ?? "").trim().toLowerCase();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const filtered = useMemo(
    () =>
      q
        ? DOCUMENT_TYPES.filter((t) =>
            t.documentTypeName.toLowerCase().includes(q),
          )
        : DOCUMENT_TYPES,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [q],
  );
  return { data: { data: { data: filtered } }, isLoading: false, error: null };
};
