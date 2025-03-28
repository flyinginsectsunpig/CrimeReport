package za.ac.cput.domain;

public enum CrimeType {
    THEFT("Theft"),
    BURGLARY("Burglary"),
    ASSAULT("Assault"),
    ROBBERY("Robbery"),
    FRAUD("Fraud"),
    VANDALISM("Vandalism"),
    OTHER("Other");

    private final String displayName;

    CrimeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
