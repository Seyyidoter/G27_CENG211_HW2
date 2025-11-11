package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an abstract class for all applications.
 * It keeps the same data and methods for every scholarship type.
 */
public abstract class Application {

    protected final String applicantId;      // applicant ID
    protected final String name;             // applicant name
    protected final double gpa;              // grade point average
    protected final double income;           // monthly income
    protected boolean transcriptValid;       // true if transcript is OK
    protected final List<Document> documents;    // all documents
    protected final List<Publication> publications; // all publications

    private static final String ENR = "ENR"; // code for enrollment document

    /**
     * Main constructor
     */
    public Application(String applicantId, String name, double gpa, double income) {
        validateConstructorParameters(applicantId, name, gpa, income);

        // remove extra spaces
        this.applicantId = applicantId.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;

        this.transcriptValid = false;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
    }

    /**
     * Copy constructor
     * Makes a new Application object with the same data.
     * Lists are copied to make new ones (not shared).
     */
    public Application(Application other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from null Application");
        }

        this.applicantId = other.applicantId;
        this.name = other.name;
        this.gpa = other.gpa;
        this.income = other.income;
        this.transcriptValid = other.transcriptValid;

        // make new lists and copy items
        this.documents = new ArrayList<>();
        for (Document d : other.documents) {
            if (d != null) {
                this.documents.add(new Document(d)); // needs copy constructor in Document
            }
        }

        this.publications = new ArrayList<>();
        for (Publication p : other.publications) {
            if (p != null) {
                this.publications.add(new Publication(p)); // needs copy constructor in Publication
            }
        }
    }

    /**
     * Check constructor parameters
     */
    private void validateConstructorParameters(String applicantId, String name, double gpa, double income) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Applicant ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        if (income < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }
    }

    /**
     * Each subclass will write its own evaluation rules.
     */
    public abstract EvaluationResult evaluate();

    /**
     * Check basic rules before giving any scholarship.
     * @return reason text if not OK, or null if all is OK
     */
    protected String performGeneralChecks() {
        // must have enrollment paper
        if (!hasDocument(ENR)) {
            return "Missing Enrollment Certificate (ENR)";
        }

        // transcript must be valid
        if (!transcriptValid) {
            return "Transcript is not verified (expected Y)";
        }

        // GPA must be 2.50 or higher
        if (gpa < 2.50) {
            return "GPA below 2.50";
        }

        // everything is OK
        return null;
    }

    /**
     * Check if this application has a document by type
     */
    public boolean hasDocument(String documentType) {
        if (documentType == null) {
            return false;
        }
        for (Document doc : documents) {
            if (doc != null && documentType.equals(doc.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a document by type
     */
    public Document getDocument(String documentType) {
        if (documentType == null) {
            return null;
        }
        for (Document doc : documents) {
            if (doc != null && documentType.equals(doc.getType())) {
                return doc;
            }
        }
        return null;
    }

    /**
     * Add one document to the list
     */
    public void addDocument(Document document) {
        if (document != null) {
            documents.add(document);
        }
    }

    /**
     * Add one publication to the list
     */
    public void addPublication(Publication publication) {
        if (publication != null) {
            publications.add(publication);
        }
    }

    /**
     * Set transcript status
     */
    public void setTranscriptValid(boolean valid) {
        this.transcriptValid = valid;
    }

    // Getters
    public String getApplicantId() {
        return applicantId;
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
     * Get copy of document list (safe)
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(new ArrayList<>(documents));
    }

    /**
     * Get copy of publication list (safe)
     */
    public List<Publication> getPublications() {
        return Collections.unmodifiableList(new ArrayList<>(publications));
    }
}
