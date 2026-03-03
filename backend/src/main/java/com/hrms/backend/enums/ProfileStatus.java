package com.hrms.backend.enums;

public enum ProfileStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave"),
    TERMINATED("Terminated");

    private final String displayName;

    ProfileStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
