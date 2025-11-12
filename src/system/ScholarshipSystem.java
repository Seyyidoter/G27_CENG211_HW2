package system;

import model.Applicant;
import model.FamilyInfo;
import model.EvaluationResult;
import model.application.Application;
import model.application.MeritApplication;
import model.application.NeedApplication;
import model.application.ResearchApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the entire scholarship evaluation process.
 * This class orchestrates loading data, creating application objects,
 * running evaluations, and storing the results.
 */
public class ScholarshipSystem {

    // Stores raw applicant data (ApplicantID -> Applicant)
    private Map<String, Applicant> applicantsMap;
    
    // Stores family info (ApplicantID -> FamilyInfo), used only by NeedApplication
    private Map<String, FamilyInfo> familyInfoMap;
    
    // Stores the specific, polymorphic Application objects to be evaluated
    private List<Application> applicationsList;
    
    // Stores the final results after evaluation
    private List<EvaluationResult> resultsList;

    private CSVReader reader;

    /**
     * Constructor for ScholarshipSystem.
     * Initializes all necessary data structures.
     */
    public ScholarshipSystem() {
        this.applicantsMap = new HashMap<>();
        this.familyInfoMap = new HashMap<>();
        this.applicationsList = new ArrayList<>();
        this.resultsList = new ArrayList<>();
        this.reader = new CSVReader(); 
    }

    /**
     * Loads all data from the specified CSV file using CSVReader.
     * Populates the applicantsMap and familyInfoMap.
     *
     * @param filePath The path to the CSV file.
     */
    public void loadData(String filePath) {
        reader.readData(filePath, applicantsMap, familyInfoMap);
    }

    /**
     * Creates specific Application objects (Merit, Need, Research)
     * based on the loaded applicant data .
     * This method uses the applicant ID prefix to determine which
     * concrete Application subclass to instantiate.
     */
    public void createApplications() {
        for (Applicant applicant : applicantsMap.values()) {
            String id = applicant.getApplicantId();
            Application app = null;

            if (id.startsWith("11")) { // Merit-based
                app = new MeritApplication(applicant);
                
            } else if (id.startsWith("22")) { // Need-based
                // Need-based applications also require the 'I' (FamilyInfo) data.
                FamilyInfo info = familyInfoMap.get(id);
                app = new NeedApplication(applicant, info);
                
            } else if (id.startsWith("33")) { // Research Grant
                app = new ResearchApplication(applicant);
                
            } else {
                System.err.println("Unknown applicant ID prefix: " + id);
                continue; // Skip this applicant
            }
            
            applicationsList.add(app);
        }
    }

    /**
     * Evaluates all created applications using polymorphism .
     * Each application type (Merit, Need, Research) will run its
     * specific evaluation logic defined in its 'evaluate()' method.
     */
    public void evaluateApplications() {
        for (Application app : applicationsList) {
            // This is polymorphism in action.
            // We call the same method 'evaluate()', but the specific
            // implementation for Merit, Need, or Research is executed.
            EvaluationResult result = app.evaluate();
            resultsList.add(result);
        }
    }

    /**
     * Returns the list of evaluation results, sorted by Applicant ID.
     *
     * @return A sorted list of EvaluationResult objects.
     */
    public List<EvaluationResult> getSortedResults() {
        // Sort the resultsList by Applicant ID
        resultsList.sort(Comparator.comparing(EvaluationResult::getApplicantId));
        
        return resultsList;
    }
}