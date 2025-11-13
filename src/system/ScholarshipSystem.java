package system;

import model.Applicant;
import model.FamilyInfo;
import model.EvaluationResult;
import applications.Application;
import applications.MeritApplication;
import applications.NeedApplication;
import applications.ResearchApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the entire scholarship evaluation process.
 */
public class ScholarshipSystem {

    private final Map<String, Applicant> applicantsMap;
    private final Map<String, FamilyInfo> familyInfoMap;
    private final List<Application> applicationsList;
    private final List<EvaluationResult> resultsList;

    private final CSVReader reader;

    public ScholarshipSystem() {
        this.applicantsMap = new HashMap<>();
        this.familyInfoMap = new HashMap<>();
        this.applicationsList = new ArrayList<>();
        this.resultsList = new ArrayList<>();
        this.reader = new CSVReader();
    }

    public void loadData(String filePath) {
        reader.readData(filePath, applicantsMap, familyInfoMap);
    }

    public void createApplications() {
        for (Applicant applicant : applicantsMap.values()) {
            String id = applicant.getId();
            Application app;

            if (id.startsWith("11")) { // Merit-based
                app = new MeritApplication(applicant);

            } else if (id.startsWith("22")) { // Need-based
                FamilyInfo info = familyInfoMap.get(id);
                app = new NeedApplication(applicant, info);

            } else if (id.startsWith("33")) { // Research Grant
                app = new ResearchApplication(applicant);

            } else {
                System.err.println("Unknown applicant ID prefix: " + id);
                continue;
            }

            applicationsList.add(app);
        }
    }

    public void evaluateApplications() {
        for (Application app : applicationsList) {
            EvaluationResult result = app.evaluate();
            resultsList.add(result);
        }
    }

    public List<EvaluationResult> getSortedResults() {
        resultsList.sort(Comparator.comparing(EvaluationResult::getApplicantId));
        return resultsList;
    }
}
