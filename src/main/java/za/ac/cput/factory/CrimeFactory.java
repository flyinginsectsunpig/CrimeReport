package za.ac.cput.factory;

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;

import java.time.LocalDateTime;

public class CrimeFactory {

    public static Crime createCrime(String description, String location, CrimeType crimeType, String reporterId) {
        return new Crime.Builder()
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .build();
    }

    public static Crime createCrimeWithTime(String description, String location, CrimeType crimeType, 
                                           String reporterId, LocalDateTime reportedAt) {
        return new Crime.Builder()
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .withReportedAt(reportedAt)
                .build();
    }

    public static Crime createResolvedCrime(String description, String location, CrimeType crimeType, 
                                           String reporterId) {
        return new Crime.Builder()
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .isResolved(true)
                .build();
    }
}
