package compulsory.panels;

public enum MovementStatus {
    RUNNING("Running"),
    STAYING("Staying"),
    CAUGHT("Caught"),
    ESCAPED("Escaped");

    private final String description;
    MovementStatus(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return this.description;
    }
}
