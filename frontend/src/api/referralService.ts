import axiosClient from "./axiosClient";

export interface ReferralPageParams {
  page: number;
  size: number;
  sort: string;
  name?: string;
  email?: string;
  "job-title"?: string;
  status?: string;
}

const referralService = {
  getAllReferrals: () => axiosClient.get("/jobs/referrals"),
  getReferralsPaginated: (params: ReferralPageParams) =>
    axiosClient.get("/jobs/referrals/filtering&pagination&sorting", { params }),
  updateReferralStatus: ({ id, status }: { id: string; status: string }) =>
    axiosClient.patch(`/jobs/referrals/${id}/status/${status}`),
};

export default referralService;
