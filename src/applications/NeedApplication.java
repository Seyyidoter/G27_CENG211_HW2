package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.Applicant;
import model.EvaluationResult;
import model.FamilyInfo;

// Need-based Scholarship: Focuses on financial need (student's own income).
public class NeedApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.NEED.toString();
    private static final DocumentType SAV = DocumentType.SAV; // Savings Document
    private final FamilyInfo familyInfo;

    public NeedApplication(Applicant applicant, FamilyInfo familyInfo) {
        super(applicant);
        if (familyInfo != null) {
            this.familyInfo = new FamilyInfo(familyInfo); // defensive copy
        } else {
            this.familyInfo = null;
        }
    }

    // Implements the specific evaluation rules for a Need-based Scholarship.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        RejectionReason generalReason = performGeneralChecks();
        if (generalReason != null) {
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, generalReason.getMessage());
        }

        // --- 2. Missing mandatory information (Priority 4) ---
        if (familyInfo == null) {
            RejectionReason reason = RejectionReason.MISSING_MANDATORY_DOCUMENT;
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 3. Calculate Adjusted Income Thresholds ---
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

        // Important: Monthly income refers ONLY to the student's own income
        double totalIncome = this.income; // familyIncome is not added

        AwardType awardType;

        // --- 4. Apply Need-Based Rules (Priority 5 Rejection) ---
        if (totalIncome <= adjustedFullThreshold) {
            awardType = AwardType.FULL;
        } else if (totalIncome <= adjustedHalfThreshold) {
            awardType = AwardType.HALF;
        } else {
            // Income > adjustedHalfThreshold -> Rejected (Priority 5)
            RejectionReason reason = RejectionReason.FINANCIAL_STATUS_UNSTABLE;
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        // --- 5. Determine Duration ---
        String duration = "1 year";

        return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, awardType.toString(), duration);
    }
}
