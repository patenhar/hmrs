import { useMemo } from "react";

const JOB_STAKEHOLDER_TYPES = [
  {
    pkJobStakeHolderTypeId: "HIRING_MANAGER",
    jobStakeHolderTypeName: "Hiring Manager",
  },
  { pkJobStakeHolderTypeId: "RECRUITER", jobStakeHolderTypeName: "Recruiter" },
  {
    pkJobStakeHolderTypeId: "INTERVIEWER",
    jobStakeHolderTypeName: "Interviewer",
  },
  { pkJobStakeHolderTypeId: "PEER", jobStakeHolderTypeName: "Peer" },
  {
    pkJobStakeHolderTypeId: "HR_BUSINESS_PARTNER",
    jobStakeHolderTypeName: "HR Business Partner",
  },
];

export const useJobStakeHolderTypes = (search: string) => {
  const q = (search ?? "").trim().toLowerCase();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const filtered = useMemo(
    () =>
      q
        ? JOB_STAKEHOLDER_TYPES.filter((t) =>
            t.jobStakeHolderTypeName.toLowerCase().includes(q),
          )
        : JOB_STAKEHOLDER_TYPES,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [q],
  );
  return { data: { data: { data: filtered } }, isLoading: false, error: null };
};
