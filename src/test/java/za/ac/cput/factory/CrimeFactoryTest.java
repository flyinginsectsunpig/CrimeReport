package za.ac.cput.factory;

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CrimeFactoryTest {

    @Test
    public void testCreateCrime_withValidData_shouldCreateCrime(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data");
        String description = "Car vandalized";
        String location = "Parking lot";
        CrimeType crimeType = CrimeType.VANDALISM;
        String reporterId = "citizen456";
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);

        System.out.println("STEP: Creating crime using CrimeFactory");
        Crime crime = CrimeFactory.createCrime(description, location, crimeType, reporterId);
        System.out.println("Crime created: " + crime);

        System.out.println("STEP: Verifying crime was created properly");
        assertNotNull(crime, "Crime should not be null");
        System.out.println("Checking ID: " + crime.getId());
        assertNotNull(crime.getId(), "Crime ID should not be null");
        
        System.out.println("STEP: Verifying crime properties match input data");
        assertEquals(description, crime.getDescription(), "Description should match");
        assertEquals(location, crime.getLocation(), "Location should match");
        assertEquals(crimeType, crime.getCrimeType(), "Crime type should match");
        assertEquals(reporterId, crime.getReporterId(), "Reporter ID should match");
        
        System.out.println("STEP: Verifying default properties");
        System.out.println("Resolution status: " + (crime.isResolved() ? "Resolved" : "Unresolved"));
        assertFalse(crime.isResolved(), "Crime should be unresolved by default");
        
        System.out.println("Reported at: " + crime.getReportedAt());
        assertNotNull(crime.getReportedAt(), "Reported time should not be null");
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCreateCrimeWithTime_withValidData_shouldCreateCrimeWithSpecificTime(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data");
        String description = "Credit card fraud";
        String location = "Online shop";
        CrimeType crimeType = CrimeType.FRAUD;
        String reporterId = "citizen789";
        LocalDateTime reportedAt = LocalDateTime.of(2023, 1, 15, 14, 30);
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);
        System.out.println("Reported At: " + reportedAt);

        System.out.println("STEP: Creating crime with specific time using CrimeFactory");
        Crime crime = CrimeFactory.createCrimeWithTime(description, location, crimeType, reporterId, reportedAt);
        System.out.println("Crime created: " + crime);

        System.out.println("STEP: Verifying crime was created properly");
        assertNotNull(crime, "Crime should not be null");
        
        System.out.println("STEP: Verifying crime properties match input data");
        assertEquals(description, crime.getDescription(), "Description should match");
        assertEquals(location, crime.getLocation(), "Location should match");
        assertEquals(crimeType, crime.getCrimeType(), "Crime type should match");
        assertEquals(reporterId, crime.getReporterId(), "Reporter ID should match");
        
        System.out.println("STEP: Verifying specific time was set correctly");
        System.out.println("Expected time: " + reportedAt);
        System.out.println("Actual time: " + crime.getReportedAt());
        assertEquals(reportedAt, crime.getReportedAt(), "Reported time should match the provided time");
        
        System.out.println("STEP: Verifying default properties");
        System.out.println("Resolution status: " + (crime.isResolved() ? "Resolved" : "Unresolved"));
        assertFalse(crime.isResolved(), "Crime should be unresolved by default");
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCreateResolvedCrime_withValidData_shouldCreateResolvedCrime(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data");
        String description = "Shoplifting";
        String location = "Convenience store";
        CrimeType crimeType = CrimeType.THEFT;
        String reporterId = "storeOwner123";
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);

        System.out.println("STEP: Creating resolved crime using CrimeFactory");
        Crime crime = CrimeFactory.createResolvedCrime(description, location, crimeType, reporterId);
        System.out.println("Crime created: " + crime);

        System.out.println("STEP: Verifying crime was created properly");
        assertNotNull(crime, "Crime should not be null");
        
        System.out.println("STEP: Verifying crime properties match input data");
        assertEquals(description, crime.getDescription(), "Description should match");
        assertEquals(location, crime.getLocation(), "Location should match");
        assertEquals(crimeType, crime.getCrimeType(), "Crime type should match");
        assertEquals(reporterId, crime.getReporterId(), "Reporter ID should match");
        
        System.out.println("STEP: Verifying crime is marked as resolved");
        System.out.println("Resolution status: " + (crime.isResolved() ? "Resolved" : "Unresolved"));
        assertTrue(crime.isResolved(), "Crime should be marked as resolved");
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCreateCrime_withInvalidData_shouldThrowException(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data with empty description (invalid)");
        String description = ""; 
        String location = "Parking lot";
        CrimeType crimeType = CrimeType.VANDALISM;
        String reporterId = "citizen456";
        System.out.println("Description: '" + description + "' (empty)");
        System.out.println("Location: " + location);
        System.out.println("Crime Type: " + crimeType);
        System.out.println("Reporter ID: " + reporterId);

        System.out.println("STEP: Attempting to create crime with invalid data (should throw exception)");
        try {
            Crime crime = CrimeFactory.createCrime(description, location, crimeType, reporterId);
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
    public void testCreateCrimeWithTime_withNullCrimeType_shouldThrowException(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        
        System.out.println("STEP: Preparing test data with null crime type (invalid)");
        String description = "Credit card fraud";
        String location = "Online shop";
        CrimeType crimeType = null; 
        String reporterId = "citizen789";
        LocalDateTime reportedAt = LocalDateTime.now();
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Crime Type: null (invalid)");
        System.out.println("Reporter ID: " + reporterId);
        System.out.println("Reported At: " + reportedAt);

        System.out.println("STEP: Attempting to create crime with null crime type (should throw exception)");
        try {
            Crime crime = CrimeFactory.createCrimeWithTime(description, location, crimeType, reporterId, reportedAt);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalStateException, but no exception was thrown");
        } catch (IllegalStateException exception) {
            System.out.println("EXPECTED: Exception thrown: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("Crime type cannot be null"), 
                    "Exception message should mention null crime type");
        }
        
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }
}
