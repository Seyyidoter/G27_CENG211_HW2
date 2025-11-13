package model;

/**
 * CSV 'I' rows: I, applicantID, familyIncome, dependents
 * Class invariant: dependents >= 0
 */
public final class FamilyInfo {
    private final double familyIncome;
    private final int dependents;

    public FamilyInfo(double familyIncome, int dependents) {
        this.familyIncome = familyIncome;
        this.dependents = Math.max(0, dependents);  // Defensive non-negative
    }

    /** Copy constructor */
    public FamilyInfo(FamilyInfo other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        this.familyIncome = other.familyIncome;
        this.dependents  = other.dependents;
    }

    public double getFamilyIncome() {
        return familyIncome;
    }
    public int getDependents() {
        return dependents;
    }

    @Override public String toString() {
        return "FamilyInfo{income=" + familyIncome + ", dependents=" + dependents + "}";
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FamilyInfo other = (FamilyInfo) o;
        return Double.compare(other.familyIncome, familyIncome) == 0 && dependents == other.dependents;
    }
}
