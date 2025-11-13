package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.Applicant;
import model.EvaluationResult;
import model.Publication;

// Research Grant: Focuses on scientific contribution (impact factor, approval).
public class ResearchApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.RESEARCH.toString();
    private static final DocumentType GRP = DocumentType.GRP; // Grant Proposal
    private static final DocumentType RSV = DocumentType.RSV; // Research Supervisor Approval

    public ResearchApplication(Applicant applicant) {
        super(applicant);
    }

    // Implements the specific evaluation rules for a Research Grant.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        RejectionReason generalReason = performGeneralChecks();
        if (generalReason != null) {
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, generalReason.getMessage());
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
                .mapToDouble(Publication::getImpactFactor)
                .sum();
        return sum / publications.size();
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
            return years + " year " + months + " months";
        }
    }
}
