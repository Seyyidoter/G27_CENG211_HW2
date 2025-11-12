import system.ScholarshipSystem;
import model.EvaluationResult;
import enums.AwardType;
import enums.RejectionReason;

import java.util.List;
import java.util.Locale;

/**
 * Main class to run the Scholarship Evaluation System.
 * (Placed in the default package / 'src' directory)
 */
public class Main {

    public static void main(String[] args) {
        // Set locale to US for consistent number formatting (e.g., 3.5 for GPA)
        Locale.setDefault(Locale.US);

    
        String filePath = "Files/ScholarshipApplications.csv";

        // 1. Initialize the system
        ScholarshipSystem system = new ScholarshipSystem();

        // 2. Load data from CSV
        system.loadData(filePath);

        // 3. Create specific application objects (Merit, Need, Research) based on data
        system.createApplications();

        // 4. Evaluate all applications
        system.evaluateApplications();

        // 5. Get the sorted results (by ID)
        List<EvaluationResult> sortedResults = system.getSortedResults();

        // 6. Print the results in the required format 
        printResults(sortedResults);
    }

    
    private static void printResults(List<EvaluationResult> results) {
        if (results.isEmpty()) {
            System.out.println("No applications were processed.");
            return;
        }

        for (EvaluationResult res : results) {
            String status = res.isAccepted() ? "Accepted" : "Rejected";
            
            System.out.print("Applicant ID: " + res.getApplicantId() +
                             ", Name: " + res.getApplicantName() +
                             ", Scholarship: " + res.getCategory().getDisplayName() +
                             ", Status: " + status);

            if (res.isAccepted()) {
                // ACCEPTED format 
                String awardType = (res.getAwardType() == AwardType.FULL) ? "Full" : "Half";
                String duration = (res.getDurationInMonths() == 24) ? "2 years" :
                                (res.getDurationInMonths() == 12) ? "1 year" :
                                res.getDurationInMonths() + " months";
                
                System.out.println(", Type: " + awardType +
                                   ", Duration: " + duration);
            } else {
                // REJECTED format 
                RejectionReason reason = res.getRejectionReason();
                System.out.println(", Reason: " + reason.getDescription());
            }
        }
    }
}