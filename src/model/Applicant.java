package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class keeps all information about one applicant.
 */
public class Applicant {

    private final String id;          // applicant ID
    private final String name;        // full name
    private final double gpa;         // grade point average
    private final double income;      // monthly income
    private boolean transcriptValid;  // true if transcript is OK
    private final List<Document> documents;     // list of documents
    private final List<Publication> publications; // list of publications

    /**
     * Main constructor.
     * It checks data before saving.
     */
    public Applicant(String id, String name, double gpa, double income) {
        // basic checks for null or invalid data
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

        // create empty lists
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
                this.documents.add(new Document(d)); // needs Document copy constructor
            }
        }

        this.publications = new ArrayList<>();
        for (Publication p : other.publications) {
            if (p != null) {
                this.publications.add(new Publication(p)); // needs Publication copy constructor
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
     * Check if this applicant has a specific document type.
     */
    public boolean hasDocument(String type) {
        if (type == null) {
            return false;
        }
        for (Document d : documents) {
            if (d != null && type.equals(d.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a document by its type.
     */
    public Document getDocument(String type) {
        if (type == null) {
            return null;
        }
        for (Document d : documents) {
            if (d != null && type.equals(d.getType())) {
                return d;
            }
        }
        return null;
    }

    /**
     * Set transcript status.
     */
    public void setTranscriptValid(boolean valid) {
        this.transcriptValid = valid;
    }

    /**
     * Find average impact factor of all publications.
     * Returns 0 if there are no publications.
     */
    public double averageImpactFactor() {
        if (publications.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        int count = 0;
        for (Publication p : publications) {
            if (p != null) {
                double val = p.getImpactFactor();
                if (val >= 0.0) {
                    sum += val;
                    count++;
                }
            }
        }
        return count == 0 ? 0.0 : (sum / count);
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
        return Collections.unmodifiableList(new ArrayList<>(documents));
    }

    /**
     * Get a safe copy of publications list.
     */
    public List<Publication> getPublications() {
        return Collections.unmodifiableList(new ArrayList<>(publications));
    }

    @Override
    public String toString() {
        return "Applicant{id='" + id + "', name='" + name + "', gpa=" + gpa +
                ", income=" + income + ", transcriptValid=" + transcriptValid +
                ", documents=" + documents.size() +
                ", publications=" + publications.size() + "}";
    }
}
