package enums;

public enum ScholarshipCategory {
    MERIT("Merit"),
    NEED("Need"),
    RESEARCH("Research");

    private final String label;

    ScholarshipCategory(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
