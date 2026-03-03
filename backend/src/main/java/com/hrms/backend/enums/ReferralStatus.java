package com.hrms.backend.enums;

public enum ReferralStatus {
    PENDING("Pending"),
    UNDER_REVIEW("Under Review"),
    HIRED("Hired"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");

    private final String displayName;

    ReferralStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
