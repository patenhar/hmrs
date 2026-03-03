package com.hrms.backend.enums;

public enum DocumentType {
    PASSPORT("Passport"),
    VISA("Visa"),
    FLIGHT_TICKET("Flight Ticket"),
    HOTEL_BOOKING("Hotel Booking"),
    TRAVEL_INSURANCE("Travel Insurance"),
    JOB_DESCRIPTION("Job Description"),
    OTHER("Other");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
