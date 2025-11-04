package model;

import enums.DocumentType;

/**
 * CSV 'D' rows: D, applicantID, documentType, durationInMonths
 * Class invariant: type != null, durationInMonths >= 0
 */
public final class Document {
    private final DocumentType type;
    private final int durationInMonths;

    public Document(DocumentType type, int durationInMonths) {
        if (type == null) throw new NullPointerException("type cannot be null");
        this.type = type;
        this.durationInMonths = Math.max(0, durationInMonths); // Defensive non-negative
    }

    /** Copy constructor (deep copy is trivial since fields are immutable) */
    public Document(Document other) {
        if (other == null){
            throw new NullPointerException("other cannot be null");
        }
        this.type = other.type;
        this.durationInMonths = other.durationInMonths;
    }

    public DocumentType getType() {
        return type;
    }
    public int getDurationInMonths() {
        return durationInMonths;
    }

    @Override public String toString() { // If we don't use, we can delete it.
        return "Document{type=" + type + ", duration=" + durationInMonths + " months}";
    }

    @Override public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Document other = (Document) o;
        return durationInMonths == other.durationInMonths && type == other.type;
    }
}
