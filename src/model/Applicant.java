package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class keeps all information about one applicant.
 */
public class Applicant {

    private final String id;          // Applicant ID
    private final String name;        // Full name
    private final double gpa;         // Grade point average
    private final double income;      // Income
    private boolean transcriptValid;  // True if transcript is OK
    private final List<Document> documents;     // List of documents
    private final List<Publication> publications; // List of publications

    /**
     * Main constructor.
     * It checks data before saving.
     */
    public Applicant(String id, String name, double gpa, double income) {
        // Basic checks for null or invalid data
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Applicant ID cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        if (income < 0.0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;
        this.transcriptValid = false;

        // Create empty lists
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
    }

    /**
     * Copy constructor.
     * Makes a new Applicant with the same data.
     * Lists are copied to new ones.
     */
    public Applicant(Applicant other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from null Applicant");
        }

        this.id = other.id;
        this.name = other.name;
        this.gpa = other.gpa;
        this.income = other.income;
        this.transcriptValid = other.transcriptValid;

        // deep copy for lists
        this.documents = new ArrayList<>();
        for (Document d : other.documents) {
            if (d != null) {
                this.documents.add(new Document(d)); // Needs Document copy constructor
            }
        }

        this.publications = new ArrayList<>();
        for (Publication p : other.publications) {
            if (p != null) {
                this.publications.add(new Publication(p)); // Needs Publication copy constructor
            }
        }
    }

    /**
     * Add one document to the list.
     */
    public void addDocument(Document doc) {
        if (doc != null) {
            documents.add(doc);
        }
    }

    /**
     * Add one publication to the list.
     */
    public void addPublication(Publication pub) {
        if (pub != null) {
            publications.add(pub);
        }
    }

    /**
     * Set transcript status.
     */
    public void setTranscriptValid(boolean valid) {
        this.transcriptValid = valid;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    public double getIncome() {
        return income;
    }

    public boolean isTranscriptValid() {
        return transcriptValid;
    }

    /**
     * Get a safe copy of documents list.
     */
    public List<Document> getDocuments() {
        return new ArrayList<>(documents);
    }

    /**
     * Get a safe copy of publications list.
     */
    public List<Publication> getPublications() {
        return new ArrayList<>(publications);
    }

    @Override
    public String toString() {
        return "Applicant{id='" + id + "', name='" + name + "', gpa=" + gpa +
                ", income=" + income + ", transcriptValid=" + transcriptValid +
                ", documents=" + documents.size() +
                ", publications=" + publications.size() + "}";
    }
}
