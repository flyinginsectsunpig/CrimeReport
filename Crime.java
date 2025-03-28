package za.ac.cput.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Crime {
    private final String id;
    private final String description;
    private final String location;
    private final LocalDateTime reportedAt;
    private final CrimeType crimeType;
    private final String reporterId;
    private final boolean isResolved;

    private Crime(Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.location = builder.location;
        this.reportedAt = builder.reportedAt;
        this.crimeType = builder.crimeType;
        this.reporterId = builder.reporterId;
        this.isResolved = builder.isResolved;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public CrimeType getCrimeType() {
        return crimeType;
    }

    public String getReporterId() {
        return reporterId;
    }

    public boolean isResolved() {
        return isResolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        return isResolved == crime.isResolved && 
               Objects.equals(id, crime.id) && 
               Objects.equals(description, crime.description) && 
               Objects.equals(location, crime.location) && 
               Objects.equals(reportedAt, crime.reportedAt) && 
               crimeType == crime.crimeType && 
               Objects.equals(reporterId, crime.reporterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, location, reportedAt, crimeType, reporterId, isResolved);
    }

    @Override
    public String toString() {
        return "Crime{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", reportedAt=" + reportedAt +
                ", crimeType=" + crimeType +
                ", reporterId='" + reporterId + '\'' +
                ", isResolved=" + isResolved +
                '}';
    }

    public static class Builder {
        private String id;
        private String description;
        private String location;
        private LocalDateTime reportedAt;
        private CrimeType crimeType;
        private String reporterId;
        private boolean isResolved;

        public Builder() {
            this.id = UUID.randomUUID().toString();
            this.reportedAt = LocalDateTime.now();
            this.isResolved = false;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withReportedAt(LocalDateTime reportedAt) {
            this.reportedAt = reportedAt;
            return this;
        }

        public Builder withCrimeType(CrimeType crimeType) {
            this.crimeType = crimeType;
            return this;
        }

        public Builder withReporterId(String reporterId) {
            this.reporterId = reporterId;
            return this;
        }

        public Builder isResolved(boolean isResolved) {
            this.isResolved = isResolved;
            return this;
        }

        public Crime build() {
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalStateException("Description cannot be empty");
            }
            if (location == null || location.trim().isEmpty()) {
                throw new IllegalStateException("Location cannot be empty");
            }
            if (crimeType == null) {
                throw new IllegalStateException("Crime type cannot be null");
            }
            if (reporterId == null || reporterId.trim().isEmpty()) {
                throw new IllegalStateException("Reporter ID cannot be empty");
            }

            return new Crime(this);
        }
    }
}
