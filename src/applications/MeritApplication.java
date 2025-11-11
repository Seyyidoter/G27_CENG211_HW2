package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.EvaluationResult;
import model.FamilyInfo; // Import might be needed if base class changes, but not for now.

//Merit-based Scholarship: Focuses on GPA and recommendations.
public class MeritApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.MERIT.toString();
    private static final String REC = DocumentType.REC.name(); // Recommendation Letter

    public MeritApplication(String applicantId, String name, double gpa, double income) {
        super(applicantId, name, gpa, income);
    }

    //@Override
    //Implements the specific evaluation rules for a Merit-based Scholarship.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        String generalCheckRejection = performGeneralChecks();
        if (generalCheckRejection != null) {
            // Map the string result back to the specific RejectionReason enum
            RejectionReason reason = mapGeneralRejection(generalCheckRejection);
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, reason.getMessage());
        }

        AwardType awardType;
        String specificRejectionReason = null;

        // --- 2. Apply Merit-Based GPA Rules ---
        if (gpa >= 3.20) {
            awardType = AwardType.FULL;
        } else if (gpa >= 3.00) {
            awardType = AwardType.HALF;
        } else {
            // GPA < 3.00 -> Rejected (Specific rule)
            awardType = null;
            specificRejectionReason = "GPA below 3.0"; // Note: This is a specific reason, not one of the priority enums.
        }

        // --- 3. Final Decision ---
        if (specificRejectionReason != null) {
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, specificRejectionReason);
        }

        // --- 4. Determine Duration ---
        String duration;
        if (hasDocument(REC)) {
            duration = "2 years";
        } else {
            duration = "1 year";
        }

        return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, awardType.toString(), duration);
    }

    //Helper to map the string from the parent's general check to the enum.
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
