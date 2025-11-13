package model;

/**
 * This class shows the result of a scholarship application.
 * It can be accepted or rejected.
 */
public class EvaluationResult {

    private final String applicantId;     // ID of the student
    private final String name;            // student name
    private final String scholarshipType; // Merit, Need, or Research
    private final boolean accepted;       // true = accepted, false = rejected
    private final String type;            // Full or Half
    private final String duration;        // How long
    private final String rejectionReason; // Why rejected

    /**
     * Constructor for accepted applications.
     */
    public EvaluationResult(String applicantId, String name, String scholarshipType,
                            String type, String duration) {

        // Simple checks for null or empty
        this.applicantId = safeValue(applicantId);
        this.name = safeValue(name);
        this.scholarshipType = safeValue(scholarshipType);

        this.accepted = true;
        this.type = safeValue(type);
        this.duration = safeValue(duration);
        this.rejectionReason = null;
    }

    /**
     * Constructor for rejected applications.
     */
    public EvaluationResult(String applicantId, String name, String scholarshipType,
                            String rejectionReason) {

        this.applicantId = safeValue(applicantId);
        this.name = safeValue(name);
        this.scholarshipType = safeValue(scholarshipType);

        this.accepted = false;
        this.type = null;
        this.duration = null;
        this.rejectionReason = safeValue(rejectionReason);
    }

    /**
     * Make a short safe text instead of null or empty.
     */
    private String safeValue(String s) {
        if (s == null || s.trim().isEmpty()) {
            return "N/A";
        }
        return s.trim();
    }

    /**
     * Make formatted result text.
     * Example:
     * Applicant ID: 1101, Name: Liam, Scholarship: Merit, Status: Accepted, Type: Full, Duration: 2 years
     */
    public String getFormattedResult() {
        StringBuilder sb = new StringBuilder();

        sb.append("Applicant ID: ").append(applicantId);
        sb.append(", Name: ").append(name);
        sb.append(", Scholarship: ").append(scholarshipType);

        if (accepted) {
            sb.append(", Status: Accepted");
            sb.append(", Type: ").append(type);
            sb.append(", Duration: ").append(duration);
        } else {
            sb.append(", Status: Rejected");
            sb.append(", Reason: ").append(rejectionReason);
        }

        return sb.toString();
    }

    // Getters
    public String getApplicantId() {
        return applicantId;
    }

    public String getName() {
        return name;
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public String toString() {
        return getFormattedResult();
    }
}
