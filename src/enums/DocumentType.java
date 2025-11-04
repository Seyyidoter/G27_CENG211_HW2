package enums;

public enum DocumentType {
    ENR,  // Enrollment Certificate (mandatory for all)
    REC,  // Recommendation Letter (academic merit)
    SAV,  // Savings Document (financial stability)
    RSV,  // Research Supervisor Approval (research grant)
    GRP;  // Grant Proposal (research grant)

    public static DocumentType fromString(String code) {
        for (DocumentType type : DocumentType.values()) {
            if (type.name().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type: " + code);
    }
}
