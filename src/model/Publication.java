package model;

/**
 * CSV 'P' rows: P, applicantID, title, impactFactor
 * Class invariant: title != null
 */
public final class Publication {
    private final String title;
    private final double impactFactor;

    public Publication(String title, double impactFactor) {
        if (title == null){
            throw new NullPointerException("title cannot be null");
        }
        this.title = title;
        this.impactFactor = impactFactor;
    }

    /** Copy constructor */
    public Publication(Publication other) {
        if (other == null) throw new NullPointerException("other cannot be null");
        this.title = other.title;
        this.impactFactor = other.impactFactor;
    }

    public String getTitle() {
        return title;
    }
    public double getImpactFactor() {
        return impactFactor;
    }

    @Override public String toString() {
        return "Publication{'" + title + "', IF=" + impactFactor + "}";
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Publication other = (Publication) o;
        return Double.compare(other.impactFactor, impactFactor) == 0 && title.equals(other.title);
    }
}
