package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.EvaluationResult;
import model.FamilyInfo;

// Need-based Scholarship: Focuses on financial need (income, dependents).
public class NeedApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.NEED.toString();
    private static final String SAV = DocumentType.SAV.name(); // Savings Document
    private final FamilyInfo familyInfo;

    public NeedApplication(String applicantId, String name, double gpa, double income, FamilyInfo familyInfo) {
        super(applicantId, name, gpa, income);
        if (familyInfo == null) {
            throw new IllegalArgumentException("FamilyInfo cannot be null for Need-based application");
        }
        this.familyInfo = familyInfo;
    }

    //@Override
    //Implements the specific evaluation rules for a Need-based Scholarship.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        String generalCheckRejection = performGeneralChecks();
        if (generalCheckRejection != null) {
            RejectionReason reason = mapGeneralRejection(generalCheckRejection);
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 2. Calculate Adjusted Income Thresholds ---
        double fullThreshold = 10000.0;
        double halfThreshold = 15000.0;
        double multiplier = 1.0;

        // Modifier 1: Savings Document (SAV) increases thresholds by 20%
        if (hasDocument(SAV)) {
            multiplier += 0.20;
        }

        // Modifier 2: Dependents (3 or more) increases thresholds by an ADDITIONAL 10%
        if (familyInfo.getDependents() >= 3) {
            multiplier += 0.10;
        }

        double adjustedFullThreshold = fullThreshold * multiplier;
        double adjustedHalfThreshold = halfThreshold * multiplier;

        AwardType awardType;

        // --- 3. Apply Need-Based Rules ---
        if (income <= adjustedFullThreshold) {
            awardType = AwardType.FULL;
        } else if (income <= adjustedHalfThreshold) {
            awardType = AwardType.HALF;
        } else {
            // Income > 15,000 (adjusted) -> Rejected (Priority 5)
            RejectionReason reason = RejectionReason.FINANCIAL_STATUS_UNSTABLE;
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 4. Determine Duration ---
        String duration = "1 year";

        return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, awardType.toString(), duration);
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
}
