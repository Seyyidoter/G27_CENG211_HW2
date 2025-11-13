package system;

import model.EvaluationResult;

import java.util.List;
import java.util.Locale;

/**
 * Main class to run the Scholarship Evaluation System.
 */
public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        String filePath = "Files/ScholarshipApplications.csv";

        ScholarshipSystem system = new ScholarshipSystem();

        system.loadData(filePath);
        system.createApplications();
        system.evaluateApplications();

        List<EvaluationResult> sortedResults = system.getSortedResults();

        printResults(sortedResults);
    }

    private static void printResults(List<EvaluationResult> results) {
        if (results.isEmpty()) {
            System.out.println("No applications were processed.");
            return;
        }

        for (EvaluationResult res : results) {
            // Already matches the required output format
            System.out.println(res.getFormattedResult());
        }
    }
}
