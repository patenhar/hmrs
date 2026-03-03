package com.hrms.backend.enums;

public enum ExpenseType {
    ACCOMMODATION("Accommodation"),
    FOOD_AND_BEVERAGE("Food & Beverage"),
    TRANSPORTATION("Transportation"),
    COMMUNICATION("Communication"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other");

    private final String displayName;

    ExpenseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
