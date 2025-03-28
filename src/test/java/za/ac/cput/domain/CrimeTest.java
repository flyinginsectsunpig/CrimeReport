package za.ac.cput.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CrimeTest {

    @Test
    public void testCrimeBuilder_withValidData_shouldCreateCrime(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data");
        String description = "Stolen laptop";
        String location = "Coffee shop on Main St";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "citizen123";
        LocalDateTime reportedAt = LocalDateTime.now();
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);
        System.out.println("Reported At: " + reportedAt);

        System.out.println("STEP: Building crime object using Builder pattern");
        Crime crime = new Crime.Builder()
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .withReportedAt(reportedAt)
                .build();
        System.out.println("Crime created: " + crime);

        System.out.println("STEP: Verifying crime object is not null");
        assertNotNull(crime, "Crime should not be null");
        
        System.out.println("STEP: Verifying ID was generated");
        System.out.println("Generated ID: " + crime.getId());
        assertNotNull(crime.getId(), "Crime ID should not be null");
        
        System.out.println("STEP: Verifying crime properties match input data");
        assertEquals(description, crime.getDescription(), "Description should match");
        assertEquals(location, crime.getLocation(), "Location should match");
        assertEquals(crimeType, crime.getCrimeType(), "Crime type should match");
        assertEquals(reporterId, crime.getReporterId(), "Reporter ID should match");
        assertEquals(reportedAt, crime.getReportedAt(), "Reported time should match");
        
        System.out.println("STEP: Verifying default resolution status is false");
        System.out.println("Resolution status: " + (crime.isResolved() ? "Resolved" : "Unresolved"));
        assertFalse(crime.isResolved(), "Crime should be unresolved by default");
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCrimeBuilder_withMissingDescription_shouldThrowException(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data with missing description");
        String location = "Coffee shop on Main St";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "citizen123";
        System.out.println("Description: <missing>");
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);

        System.out.println("STEP: Attempting to build crime without description (should throw exception)");
        try {
            Crime crime = new Crime.Builder()
                    .withLocation(location)
                    .withCrimeType(crimeType)
                    .withReporterId(reporterId)
                    .build();
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalStateException, but no exception was thrown");
        } catch (IllegalStateException exception) {
            System.out.println("EXPECTED: Exception thrown: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("Description cannot be empty"), 
                    "Exception message should mention empty description");
        }
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCrimeBuilder_withMissingLocation_shouldThrowException() {

        String description = "Stolen laptop";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "citizen123";

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new Crime.Builder()
                    .withDescription(description)
                    .withCrimeType(crimeType)
                    .withReporterId(reporterId)
                    .build();
        });

        assertTrue(exception.getMessage().contains("Location cannot be empty"));
    }

    @Test
    public void testCrimeBuilder_withMissingCrimeType_shouldThrowException() {

        String description = "Stolen laptop";
        String location = "Coffee shop on Main St";
        String reporterId = "citizen123";

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new Crime.Builder()
                    .withDescription(description)
                    .withLocation(location)
                    .withReporterId(reporterId)
                    .build();
        });

        assertTrue(exception.getMessage().contains("Crime type cannot be null"));
    }

    @Test
    public void testCrimeBuilder_withMissingReporterId_shouldThrowException() {

        String description = "Stolen laptop";
        String location = "Coffee shop on Main St";
        CrimeType crimeType = CrimeType.THEFT;

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new Crime.Builder()
                    .withDescription(description)
                    .withLocation(location)
                    .withCrimeType(crimeType)
                    .build();
        });

        assertTrue(exception.getMessage().contains("Reporter ID cannot be empty"));
    }

    @Test
    public void testCrimeBuilder_withResolvedStatus_shouldCreateResolvedCrime() {

        String description = "Stolen laptop";
        String location = "Coffee shop on Main St";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "citizen123";
        boolean isResolved = true;

        Crime crime = new Crime.Builder()
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .isResolved(isResolved)
                .build();

        assertTrue(crime.isResolved());
    }

    @Test
    public void testCrimeEquality_withSameData_shouldBeEqual() {

        String id = "crime-123";
        String description = "Stolen laptop";
        String location = "Coffee shop on Main St";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "citizen123";
        LocalDateTime reportedAt = LocalDateTime.of(2023, 1, 1, 12, 0);

        Crime crime1 = new Crime.Builder()
                .withId(id)
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .withReportedAt(reportedAt)
                .build();

        Crime crime2 = new Crime.Builder()
                .withId(id)
                .withDescription(description)
                .withLocation(location)
                .withCrimeType(crimeType)
                .withReporterId(reporterId)
                .withReportedAt(reportedAt)
                .build();

        assertEquals(crime1, crime2);
        assertEquals(crime1.hashCode(), crime2.hashCode());
    }

    @Test
    public void testCrimeEquality_withDifferentData_shouldNotBeEqual() {

        String id = "crime-123";

        Crime crime1 = new Crime.Builder()
                .withId(id)
                .withDescription("Stolen laptop")
                .withLocation("Coffee shop on Main St")
                .withCrimeType(CrimeType.THEFT)
                .withReporterId("citizen123")
                .build();

        Crime crime2 = new Crime.Builder()
                .withId(id)
                .withDescription("Broken window")
                .withLocation("Library")
                .withCrimeType(CrimeType.VANDALISM)
                .withReporterId("citizen456")
                .build();

        assertNotEquals(crime1, crime2);
    }
}
