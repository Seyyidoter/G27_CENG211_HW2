package applications;

import enums.DocumentType;
import enums.RejectionReason;
import model.Applicant;
import model.Document;
import model.EvaluationResult;
import model.Publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an abstract class for all applications.
 * It keeps the same data and methods for every scholarship type.
 */
public abstract class Application {

    protected final String applicantId;          // Applicant ID
    protected final String name;                 // Applicant name
    protected final double gpa;                  // Grade point average
    protected final double income;               // Monthly income
    protected boolean transcriptValid;           // true if transcript is ok
    protected final List<Document> documents;    // All documents
    protected final List<Publication> publications; // All publications

    private static final DocumentType ENR = DocumentType.ENR; // Enrollment document

    /**
     * Main constructor
     */
    public Application(String applicantId, String name, double gpa, double income) {
        validateConstructorParameters(applicantId, name, gpa, income);

        // Remove extra spaces
        this.applicantId = applicantId.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;

        this.transcriptValid = false;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
    }

    /**
     * Constructor from Applicant (deep copy).
     */
    public Application(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Cannot create Application from null Applicant");
        }

        validateConstructorParameters(applicant.getId(), applicant.getName(), applicant.getGpa(), applicant.getIncome());

        this.applicantId = applicant.getId().trim();
        this.name = applicant.getName().trim();
        this.gpa = applicant.getGpa();
        this.income = applicant.getIncome();
        this.transcriptValid = applicant.isTranscriptValid();

        // Copy documents
        this.documents = new ArrayList<>();
        for (Document d : applicant.getDocuments()) {
            if (d != null) {
                this.documents.add(new Document(d));
            }
        }

        // Copy publications
        this.publications = new ArrayList<>();
        for (Publication p : applicant.getPublications()) {
            if (p != null) {
                this.publications.add(new Publication(p));
            }
        }
    }

    /**
     * Copy constructor
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

        this.documents = new ArrayList<>();
        for (Document d : other.documents) {
            if (d != null) {
                this.documents.add(new Document(d));
            }
        }

        this.publications = new ArrayList<>();
        for (Publication p : other.publications) {
            if (p != null) {
                this.publications.add(new Publication(p));
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
     *
     * @return RejectionReason if not OK, or null if all is OK
     */
    protected RejectionReason performGeneralChecks() {
        // Must have enrollment paper
        if (!hasDocument(ENR)) {
            return RejectionReason.MISSING_ENROLLMENT;
        }

        // Transcript must be valid
        if (!transcriptValid) {
            return RejectionReason.MISSING_TRANSCRIPT;
        }

        // GPA must be 2.50 or higher
        if (gpa < 2.50) {
            return RejectionReason.GPA_BELOW_MINIMUM;
        }
        return null;
    }

    public boolean hasDocument(DocumentType type) {
        if (type == null) {
            return false;
        }
        for (Document d : documents) {
            if (d != null && d.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Document getDocument(DocumentType type) {
        if (type == null) {
            return null;
        }
        for (Document d : documents) {
            if (d != null && d.getType() == type) {
                return d;
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
