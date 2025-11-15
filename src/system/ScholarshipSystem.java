package system;

import applications.Application;
import applications.MeritApplication;
import applications.NeedApplication;
import applications.ResearchApplication;
import model.Applicant;
import model.EvaluationResult;
import model.FamilyInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages the entire scholarship evaluation process.
 * Uses only ArrayLists (no Maps).
 */
public class ScholarshipSystem {

    private final List<Applicant> applicantsList;
    private final List<String> familyInfoApplicantIds;
    private final List<FamilyInfo> familyInfoList;
    private final List<Application> applicationsList;
    private final List<EvaluationResult> resultsList;

    private final CSVReader reader;

    public ScholarshipSystem() {
        this.applicantsList = new ArrayList<>();
        this.familyInfoApplicantIds = new ArrayList<>();
        this.familyInfoList = new ArrayList<>();
        this.applicationsList = new ArrayList<>();
        this.resultsList = new ArrayList<>();
        this.reader = new CSVReader();
    }

    public void loadData(String filePath) {
        reader.readData(filePath, applicantsList, familyInfoApplicantIds, familyInfoList);
    }

    public void createApplications() {
        for (Applicant applicant : applicantsList) {
            String id = applicant.getId();
            Application app = null;

            if (id.startsWith("11")) {             // Merit-based
                app = new MeritApplication(applicant);

            } else if (id.startsWith("22")) {      // Need-based
                FamilyInfo info = findFamilyInfoFor(id);
                app = new NeedApplication(applicant, info);

            } else if (id.startsWith("33")) {      // Research Grant
                app = new ResearchApplication(applicant);

            } else {
                System.err.println("Unknown applicant ID prefix: " + id);
            }

            if (app != null) {
                applicationsList.add(app);
            }
        }
    }

    public void evaluateApplications() {
        resultsList.clear();
        for (Application app : applicationsList) {
            EvaluationResult result = app.evaluate();
            resultsList.add(result);
        }
    }

    public List<EvaluationResult> getSortedResults() {
        resultsList.sort(Comparator.comparing(EvaluationResult::getApplicantId));
        return resultsList;
    }

    /**
     * Finds the FamilyInfo for the given applicant ID by searching in the lists.
     *
     * @param applicantId ID of the applicant
     * @return FamilyInfo object if found, otherwise null
     */
    private FamilyInfo findFamilyInfoFor(String applicantId) {
        for (int i = 0; i < familyInfoApplicantIds.size(); i++) {
            if (familyInfoApplicantIds.get(i).equals(applicantId)) {
                return familyInfoList.get(i);
            }
        }
        return null;
    }
}
