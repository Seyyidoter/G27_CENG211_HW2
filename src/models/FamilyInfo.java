package models;

/** For CSV 'I' rows: I, applicantID, familyIncome, dependents */
public final class FamilyInfo {
    private final double familyIncome; // monthly
    private final int dependents;

    public FamilyInfo(double familyIncome, int dependents) {
        this.familyIncome = familyIncome;
        this.dependents = Math.max(0, dependents);
    }

    public double getFamilyIncome() {
        return familyIncome;
    }

    public int getDependents() {
        return dependents;
    }

    @Override //If we wont use we can delete it.
    public String toString() {
        return "FamilyInfo{income=" + familyIncome + ", dependents=" + dependents + "}";
    }
}
