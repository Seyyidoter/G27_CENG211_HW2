package system;

import enums.DocumentType;
import model.Applicant;
import model.Document;
import model.FamilyInfo;
import model.Publication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the ScholarshipApplications.csv file, parses the data,
 * and populates the provided lists (no Maps, only ArrayLists).
 */
public class CSVReader {

    /**
     * Reads the CSV file and populates the given lists with applicant data.
     * Uses an ArrayList-based approach (no Map/HashMap).
     *
     * @param filePath               Path to the CSV file.
     * @param applicantsList         List to fill with Applicant objects.
     * @param familyInfoApplicantIds List of applicant IDs corresponding to each FamilyInfo.
     * @param familyInfoList         List of FamilyInfo objects.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void readData(String filePath,
                         List<Applicant> applicantsList,
                         List<String> familyInfoApplicantIds,
                         List<FamilyInfo> familyInfoList) {

        if (applicantsList == null || familyInfoApplicantIds == null || familyInfoList == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }

        // Clear old data in case loadData is called more than once
        applicantsList.clear();
        familyInfoApplicantIds.clear();
        familyInfoList.clear();

        // 1) Read all lines into memory
        List<String[]> allLines = new ArrayList<>();

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

                allLines.add(parts);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
            return;
        }

        // 2) Collect all distinct applicant IDs
        List<String> applicantIds = new ArrayList<>();
        for (String[] parts : allLines) {
            String applicantId = parts[1].trim();
            if (!applicantIds.contains(applicantId)) {
                applicantIds.add(applicantId);
            }
        }

        // 3) For each applicant ID, process its lines and build an Applicant
        for (String applicantId : applicantIds) {

            // Collect all lines belonging to this applicant
            List<String[]> linesForApplicant = new ArrayList<>();
            for (String[] parts : allLines) {
                String id = parts[1].trim();
                if (id.equals(applicantId)) {
                    linesForApplicant.add(parts);
                }
            }

            // Find the 'A' line (Applicant base information)
            String[] aLine = null;
            for (String[] parts : linesForApplicant) {
                if (parts[0].trim().equals("A")) {
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
                // A, applicantID, name, GPA, income
                String name = aLine[2];
                double gpa = Double.parseDouble(aLine[3].trim());
                double income = Double.parseDouble(aLine[4].trim());
                applicant = new Applicant(applicantId, name, gpa, income);
            } catch (Exception e) {
                System.err.println("Skipping applicant " + applicantId + ": Error processing 'A' line. " + e.getMessage());
                continue;
            }

            // 4) Process all other lines for this applicant
            for (String[] parts : linesForApplicant) {
                String prefix = parts[0].trim();

                try {
                    switch (prefix) {
                        case "A":
                            // Already processed above
                            break;

                        case "D":
                            // D, applicantID, documentType, durationInMonths
                            if (parts.length < 4) {
                                System.err.println("Skipping malformed 'D' line for applicant " + applicantId);
                                break;
                            }
                            DocumentType type = DocumentType.fromString(parts[2].trim());
                            int duration = Integer.parseInt(parts[3].trim());
                            applicant.addDocument(new Document(type, duration));
                            break;

                        case "P":
                            // P, applicantID, title, impactFactor
                            if (parts.length < 4) {
                                System.err.println("Skipping malformed 'P' line for applicant " + applicantId);
                                break;
                            }
                            String title = parts[2];
                            double impact = Double.parseDouble(parts[3].trim());
                            applicant.addPublication(new Publication(title, impact));
                            break;

                        case "T":
                            // T, applicantID, Y/N
                            if (parts.length < 3) {
                                System.err.println("Skipping malformed 'T' line for applicant " + applicantId);
                                break;
                            }
                            boolean valid = parts[2].trim().equalsIgnoreCase("Y");
                            applicant.setTranscriptValid(valid);
                            break;

                        case "I":
                            // I, applicantID, familyIncome, dependents
                            if (parts.length < 4) {
                                System.err.println("Skipping malformed 'I' line for applicant " + applicantId);
                                break;
                            }
                            double familyIncome = Double.parseDouble(parts[2].trim());
                            int dependents = Integer.parseInt(parts[3].trim());
                            FamilyInfo info = new FamilyInfo(familyIncome, dependents);

                            // If multiple 'I' lines exist for the same applicant,
                            // keep the last one (overwrite previous)
                            int existingIndex = familyInfoApplicantIds.indexOf(applicantId);
                            if (existingIndex == -1) {
                                familyInfoApplicantIds.add(applicantId);
                                familyInfoList.add(info);
                            } else {
                                familyInfoList.set(existingIndex, info);
                            }
                            break;

                        default:
                            System.err.println("Skipping unknown line type: " + prefix + " (Applicant: " + applicantId + ")");
                    }
                } catch (Exception e) {
                    System.err.println(
                            "Skipping malformed line for applicant " + applicantId + ": "
                                    + String.join(",", parts) + " | Error: " + e.getMessage()
                    );
                }
            }

            // Add the fully constructed Applicant object to the main list
            applicantsList.add(applicant);
        }
    }
}
