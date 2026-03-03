import {
  useMutation,
  useQuery,
  useQueryClient,
  keepPreviousData,
} from "@tanstack/react-query";
import { useMemo } from "react";
import { toast } from "sonner";
import referralService from "../referralService";
import type { ReferralPageParams } from "../referralService";

const { getAllReferrals, getReferralsPaginated, updateReferralStatus } =
  referralService;

export const useGetAllReferrals = () => {
  return useQuery({
    queryKey: ["Referrals"],
    queryFn: () => getAllReferrals(),
  });
};

export const useGetReferralsPaginated = (params: ReferralPageParams) => {
  return useQuery({
    queryKey: ["Referrals", "paginated", params],
    queryFn: () => getReferralsPaginated(params),
    placeholderData: keepPreviousData,
  });
};

const REFERRAL_STATUSES = [
  { pkReferralStatusId: "PENDING", referralStatusName: "Pending" },
  { pkReferralStatusId: "UNDER_REVIEW", referralStatusName: "Under Review" },
  { pkReferralStatusId: "HIRED", referralStatusName: "Hired" },
  { pkReferralStatusId: "REJECTED", referralStatusName: "Rejected" },
  { pkReferralStatusId: "WITHDRAWN", referralStatusName: "Withdrawn" },
];

export const useGetAllReferralStatuses = () => {
  return {
    data: { data: { data: REFERRAL_STATUSES } },
    isLoading: false,
    error: null,
  };
};

export const useSearchReferralStatuses = (search: string) => {
  const q = (search ?? "").trim().toLowerCase();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const filtered = useMemo(
    () =>
      q
        ? REFERRAL_STATUSES.filter((s) =>
            s.referralStatusName.toLowerCase().includes(q),
          )
        : REFERRAL_STATUSES,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [q],
  );
  return { data: { data: { data: filtered } }, isLoading: false, error: null };
};

export const useUpdateReferralStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateReferralStatus,
    onSuccess: (res) => {
      toast.success(res.data.message);
      queryClient.invalidateQueries({ queryKey: ["Referrals"] });
    },
    onError: (error) => {
      toast.error("Referral status update failed", {
        description: error.message || "Something went wrong",
      });
    },
  });
};
