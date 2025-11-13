package system;

import model.Applicant;
import model.Document;
import model.FamilyInfo;
import model.Publication;
import enums.DocumentType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the ScholarshipApplications.csv file, parses the data,
 * and populates the provided data maps.
 */
public class CSVReader {

    /**
     * Reads the CSV file and populates the given maps with applicant data.
     * Uses a two-pass approach to handle the out-of-order rows.
     *
     * @param filePath Path to the CSV file.
     * @param applicantsMap The map of Applicants to populate (ApplicantID -> Applicant).
     * @param familyInfoMap The map of FamilyInfo to populate (ApplicantID -> FamilyInfo).
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void readData(String filePath, Map<String, Applicant> applicantsMap, Map<String, FamilyInfo> familyInfoMap) {
        if (applicantsMap == null || familyInfoMap == null) {
            throw new IllegalArgumentException("Maps cannot be null");
        }
        // This map temporarily groups all lines by applicant ID.
        Map<String, List<String[]>> linesByApplicant = new HashMap<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }
                String applicantId = parts[1];
                // Create a list for this ID or add to the existing list
                linesByApplicant.computeIfAbsent(applicantId, k -> new ArrayList<>()).add(parts);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
            return; // Stop processing if file cannot be read
        }

        // Create objects from the grouped lines
        for (Map.Entry<String, List<String[]>> entry : linesByApplicant.entrySet()) {
            String applicantId = entry.getKey();
            List<String[]> lines = entry.getValue();

            // 1. Find the 'A' (Applicant) line to create the base object
            String[] aLine = null;
            for (String[] parts : lines) {
                if (parts[0].equals("A")) {
                    aLine = parts;
                    break;
                }
            }

            if (aLine == null) {
                System.err.println("Skipping applicant " + applicantId + ": 'A' (Applicant) line not found.");
                continue; // Mandatory 'A' line is missing
            }

            Applicant applicant;
            try {
                // Create the Applicant object
                String name = aLine[2];
                double gpa = Double.parseDouble(aLine[3]);
                double income = Double.parseDouble(aLine[4]);
                applicant = new Applicant(applicantId, name, gpa, income);
            
            } catch (Exception e) {
                System.err.println("Skipping applicant " + applicantId + ": Error processing 'A' line. " + e.getMessage());
                continue;
            }

            // 2. Process all other lines for this applicant
            for (String[] parts : lines) {
                try {
                    switch (parts[0]) {
                        case "A":
                            // Already processed
                            break;
                        case "D":
                            // D, applicantID, documentType, durationInMonths
                            DocumentType type = DocumentType.fromString(parts[2]);
                            int duration = Integer.parseInt(parts[3]);
                            applicant.addDocument(new Document(type, duration));
                            break;
                        case "P":
                            // P, applicantID, title, impactFactor
                            String title = parts[2];
                            double impact = Double.parseDouble(parts[3]);
                            applicant.addPublication(new Publication(title, impact));
                            break;
                        case "T":
                            // T, applicantID, Y/N
                            boolean valid = parts[2].equalsIgnoreCase("Y");
                            applicant.setTranscriptValid(valid);
                            break;
                        case "I":
                            // I, applicantID, familyIncome, dependents
                            double familyIncome = Double.parseDouble(parts[2]);
                            int dependents = Integer.parseInt(parts[3]);
                            // Store in the separate map
                            familyInfoMap.put(applicantId, new FamilyInfo(familyIncome, dependents));
                            break;
                        default:
                            System.err.println("Skipping unknown line type: " + parts[0] + " (Applicant: " + applicantId + ")");
                    }
                } catch (Exception e) {
                    // An error in one line (e.g., parsing) shouldn't stop the whole applicant
                    System.err.println("Skipping malformed line for applicant " + applicantId + ": " + String.join(",", parts) + " | Error: " + e.getMessage());
                }
            }

            // Add the fully constructed Applicant object to the main map
            applicantsMap.put(applicantId, applicant);
        }
    }
}
