package enums;

public enum RejectionReason {
    MISSING_ENROLLMENT("Missing Enrollment Certificate"),
    MISSING_TRANSCRIPT("Missing Transcript"),
    GPA_BELOW_MINIMUM("GPA below 2.5"),
    MISSING_MANDATORY_DOCUMENT("Missing mandatory document"),
    FINANCIAL_STATUS_UNSTABLE("Financial status unstable"),
    MISSING_PUBLICATION_OR_PROPOSAL("Missing publication or proposal"),
    PUBLICATION_IMPACT_TOO_LOW("Publication impact too low");

    private final String message;

    RejectionReason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
