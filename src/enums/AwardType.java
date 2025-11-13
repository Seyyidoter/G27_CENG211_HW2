package enums;

public enum AwardType {
    FULL("Full"),
    HALF("Half");

    private final String label;

    AwardType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
