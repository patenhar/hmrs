package com.hrms.backend.enums;

public enum JobStakeHolderType {
    HIRING_MANAGER("Hiring Manager"),
    RECRUITER("Recruiter"),
    INTERVIEWER("Interviewer"),
    PEER("Peer"),
    HR_BUSINESS_PARTNER("HR Business Partner");

    private final String displayName;

    JobStakeHolderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
