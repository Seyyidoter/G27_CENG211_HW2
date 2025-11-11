package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.EvaluationResult;

// Research Grant: Focuses on scientific contribution (impact factor, approval).
public class ResearchApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.RESEARCH.toString();
    private static final String GRP = DocumentType.GRP.name(); // Grant Proposal
    private static final String RSV = DocumentType.RSV.name(); // Research Supervisor Approval

    public ResearchApplication(String applicantId, String name, double gpa, double income) {
        super(applicantId, name, gpa, income);
    }

    // @Override
    // Implements the specific evaluation rules for a Research Grant.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        String generalCheckRejection = performGeneralChecks();
        if (generalCheckRejection != null) {
            RejectionReason reason = mapGeneralRejection(generalCheckRejection);
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 2. Check Mandatory Research Documents/Publications (Priority 6) ---
        boolean hasPublications = !publications.isEmpty();
        boolean hasGrantProposal = hasDocument(GRP);

        if (!hasPublications && !hasGrantProposal) {
            RejectionReason reason = RejectionReason.MISSING_PUBLICATION_OR_PROPOSAL;
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 3. Calculate Average Impact Factor ---
        double avgImpact = calculateAverageImpactFactor();
        AwardType awardType;
        String baseDuration;

        // --- 4. Apply Research Grant Rules (Priority 7 Rejection) ---
        if (avgImpact >= 1.50) {
            awardType = AwardType.FULL;
            baseDuration = "1 year";
        } else if (avgImpact >= 1.00) {
            awardType = AwardType.HALF;
            baseDuration = "6 months";
        } else {
            // Avg impact < 1.00 -> Rejected
            RejectionReason reason = RejectionReason.PUBLICATION_IMPACT_TOO_LOW;
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 5. Determine Final Duration ---
        // If RSV exists, extend duration by +1 year.
        if (hasDocument(RSV)) {
            // Simple logic for duration extension: only "1 year" becomes "2 years", 
            // and "6 months" becomes "1 year 6 months" (or "1.5 years").
            // To keep it simple based on the usual assignment style, we'll convert to months, add 12, and format back.
            int durationInMonths = baseDuration.contains("1 year") ? 12 : 6;
            durationInMonths += 12; // Add 1 year (12 months)

            baseDuration = formatDuration(durationInMonths);
        }

        return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, awardType.toString(), baseDuration);
    }

    // Helper to calculate average impact factor for all publications.
    private double calculateAverageImpactFactor() {
        if (publications.isEmpty()) {
            return 0.0;
        }

        double sum = publications.stream()
                                 .mapToDouble(p -> p.getImpactFactor())
                                 .sum();
        return sum / publications.size();
    }

    // Helper to map the string from the parent's general check to the enum.
    private RejectionReason mapGeneralRejection(String checkResult) {
        if (checkResult.contains("Enrollment")) {
            return RejectionReason.MISSING_ENROLLMENT;
        }
        if (checkResult.contains("Transcript")) {
            return RejectionReason.MISSING_TRANSCRIPT;
        }
        if (checkResult.contains("2.50")) {
            return RejectionReason.GPA_BELOW_MINIMUM;
        }
        return RejectionReason.MISSING_MANDATORY_DOCUMENT;
    }

    // Helper to format duration from months to years/months string.
    private String formatDuration(int totalMonths) {
        int years = totalMonths / 12;
        int months = totalMonths % 12;

        if (months == 0) {
            return years + " years";
        } else if (years == 0) {
            return months + " months";
        } else {
            // E.g., 1 year 6 months
            return years + " year " + months + " months";
        }
    }
}
