package applications;

import enums.AwardType;
import enums.DocumentType;
import enums.RejectionReason;
import enums.ScholarshipCategory;
import model.Applicant;
import model.EvaluationResult;

// Merit-based Scholarship: Focuses on GPA and recommendations.
public class MeritApplication extends Application {

    private static final String SCHOLARSHIP_TYPE = ScholarshipCategory.MERIT.toString();
    private static final DocumentType REC = DocumentType.REC; // Recommendation Letter

    public MeritApplication(Applicant applicant) {
        super(applicant);
    }

    // Implements the specific evaluation rules for a Merit-based Scholarship.
    @Override
    public EvaluationResult evaluate() {
        // --- 1. Perform General Checks (Priority 1-3 Rejections) ---
        RejectionReason generalReason = performGeneralChecks();
        if (generalReason != null) {
            return new EvaluationResult(applicantId, name, SCHOLARSHIP_TYPE, generalReason.getMessage());
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
            specificRejectionReason = "GPA below 3.0";
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
}
