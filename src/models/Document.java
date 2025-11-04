package models;

import enums.DocumentType;

/**
 * A single document attached to an application.
 * For the CSV 'D' rows: D, applicantID, documentType, durationInMonths
 */
public final class Document {
    private final DocumentType type;
    private final int durationInMonths; // may be 0 if not applicable

    public Document(DocumentType type, int durationInMonths) {
        if (type == null) {
            throw new NullPointerException("type cannot be null");
        }
        this.type = type;

        // Prevent negative durations
        this.durationInMonths = Math.max(durationInMonths, 0);
    }

    public DocumentType getType() {
        return type;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    @Override
    public String toString() {
        return "Document{type=" + type + ", duration=" + durationInMonths + " months}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Document other = (Document) o;
        return this.durationInMonths == other.durationInMonths && this.type == other.type;
    }

}
