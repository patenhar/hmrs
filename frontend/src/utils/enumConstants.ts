export type EnumOption = { id: string; label: string };

export const DOCUMENT_TYPE_OPTIONS: EnumOption[] = [
  { id: "PASSPORT", label: "Passport" },
  { id: "VISA", label: "Visa" },
  { id: "FLIGHT_TICKET", label: "Flight Ticket" },
  { id: "HOTEL_BOOKING", label: "Hotel Booking" },
  { id: "TRAVEL_INSURANCE", label: "Travel Insurance" },
  { id: "JOB_DESCRIPTION", label: "Job Description" },
];

export const EXPENSE_TYPE_OPTIONS: EnumOption[] = [
  { id: "ACCOMMODATION", label: "Accommodation" },
  { id: "FOOD_AND_BEVERAGE", label: "Food & Beverage" },
  { id: "TRANSPORTATION", label: "Transportation" },
  { id: "COMMUNICATION", label: "Communication" },
  { id: "ENTERTAINMENT", label: "Entertainment" },
  { id: "OTHER", label: "Other" },
];

export const JOB_STAKE_HOLDER_TYPE_OPTIONS: EnumOption[] = [
  { id: "HIRING_MANAGER", label: "Hiring Manager" },
  { id: "RECRUITER", label: "Recruiter" },
  { id: "INTERVIEWER", label: "Interviewer" },
  { id: "PEER", label: "Peer" },
  { id: "HR_BUSINESS_PARTNER", label: "HR Business Partner" },
];

export const REFERRAL_STATUS_OPTIONS: EnumOption[] = [
  { id: "PENDING", label: "Pending" },
  { id: "UNDER_REVIEW", label: "Under Review" },
  { id: "HIRED", label: "Hired" },
  { id: "REJECTED", label: "Rejected" },
  { id: "WITHDRAWN", label: "Withdrawn" },
];

export function makeStaticQueryResult<T extends { label: string }>(
  options: T[],
  search: string,
) {
  const q = (search ?? "").trim().toLowerCase();
  const filtered = q
    ? options.filter((o) => o.label.toLowerCase().includes(q))
    : options;
  return { data: { data: { data: filtered } }, isLoading: false, error: null };
}
