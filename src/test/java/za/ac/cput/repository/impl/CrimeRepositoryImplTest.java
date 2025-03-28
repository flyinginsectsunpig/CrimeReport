package za.ac.cput.repository.impl;

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;
import za.ac.cput.factory.CrimeFactory;
import za.ac.cput.repository.CrimeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CrimeRepositoryImplTest {

    private CrimeRepositoryImpl repository;
    private Crime testCrime;
    private final String TEST_DESCRIPTION = "Bike theft";
    private final String TEST_LOCATION = "City park";
    private final CrimeType TEST_CRIME_TYPE = CrimeType.THEFT;
    private final String TEST_REPORTER_ID = "citizen123";

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("\n---- STARTING TEST: " + testInfo.getDisplayName() + " ----");
        repository = CrimeRepositoryImpl.getRepository();
        repository.clearRepository();
        System.out.println("Repository cleared for clean test environment");
        testCrime = CrimeFactory.createCrime(TEST_DESCRIPTION, TEST_LOCATION, TEST_CRIME_TYPE, TEST_REPORTER_ID);
        System.out.println("Created test crime: " + testCrime.toString());
    }
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        repository.clearRepository();
        System.out.println("Repository cleared after test");
        System.out.println("---- FINISHED TEST: " + testInfo.getDisplayName() + " ----\n");
    }

    @Test
    public void testCreate_withValidCrime_shouldAddToRepository(TestInfo testInfo) {
        System.out.println("STEP: Creating a valid crime");
        Crime savedCrime = repository.create(testCrime);
        System.out.println("Crime saved to repository: " + savedCrime);

        System.out.println("STEP: Verifying the crime was saved correctly");
        assertEquals(testCrime, savedCrime);
        
        System.out.println("STEP: Checking repository size");
        int size = repository.readAll().size();
        System.out.println("Repository now contains " + size + " crime(s)");
        assertEquals(1, size);
    }

    @Test
    public void testCreate_withNullCrime_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to create a null crime (should throw exception)");
        try {
            repository.create(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreate_withDuplicateId_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Creating initial crime");
        Crime savedCrime = repository.create(testCrime);
        System.out.println("Initial crime saved with ID: " + savedCrime.getId());

        System.out.println("STEP: Creating duplicate crime with same ID but different details");
        Crime duplicateCrime = new Crime.Builder()
                .withId(testCrime.getId())
                .withDescription("Different description")
                .withLocation("Different location")
                .withCrimeType(CrimeType.BURGLARY)
                .withReporterId("differentReporter")
                .build();
        System.out.println("Duplicate crime created: " + duplicateCrime);

        System.out.println("STEP: Attempting to save duplicate crime (should throw exception)");
        try {
            repository.create(duplicateCrime);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRead_withExistingId_shouldReturnCrime(TestInfo testInfo) {
        System.out.println("STEP: Creating and saving a crime");
        Crime savedCrime = repository.create(testCrime);
        System.out.println("Crime saved with ID: " + savedCrime.getId());

        System.out.println("STEP: Reading crime by ID: " + testCrime.getId());
        Optional<Crime> result = repository.read(testCrime.getId());
        
        System.out.println("STEP: Verifying crime was retrieved successfully");
        assertTrue(result.isPresent(), "Crime should be found");
        System.out.println("Retrieved crime: " + result.get());
        assertEquals(testCrime, result.get(), "Retrieved crime should match the original");
    }

    @Test
    public void testRead_withNonExistingId_shouldReturnEmptyOptional(TestInfo testInfo) {
        String nonExistentId = "non-existing-id";
        System.out.println("STEP: Attempting to read a non-existent crime with ID: " + nonExistentId);
        
        Optional<Crime> result = repository.read(nonExistentId);
        
        System.out.println("STEP: Verifying no crime was found");
        assertFalse(result.isPresent(), "No crime should be found");
        System.out.println("Result is empty as expected");
    }

    @Test
    public void testRead_withNullId_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to read a crime with null ID (should throw exception)");
        
        try {
            repository.read(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadAll_withMultipleCrimes_shouldReturnAllCrimes(TestInfo testInfo) {
        System.out.println("STEP: Creating first test crime");
        Crime crime1 = repository.create(testCrime);
        System.out.println("First crime saved: " + crime1);

        System.out.println("STEP: Creating second test crime");
        Crime anotherCrime = CrimeFactory.createCrime(
                "Vandalized car",
                "Downtown parking lot",
                CrimeType.VANDALISM,
                "citizen456"
        );
        Crime crime2 = repository.create(anotherCrime);
        System.out.println("Second crime saved: " + crime2);

        System.out.println("STEP: Retrieving all crimes from repository");
        List<Crime> allCrimes = repository.readAll();
        System.out.println("Retrieved " + allCrimes.size() + " crime(s) from repository");

        System.out.println("STEP: Verifying all crimes were retrieved");
        assertEquals(2, allCrimes.size(), "Repository should contain exactly 2 crimes");
        System.out.println("STEP: Verifying first crime is in the result list");
        assertTrue(allCrimes.contains(testCrime), "First crime should be in the result list");
        System.out.println("STEP: Verifying second crime is in the result list");
        assertTrue(allCrimes.contains(anotherCrime), "Second crime should be in the result list");
    }

    @Test
    public void testReadAll_withEmptyRepository_shouldReturnEmptyList(TestInfo testInfo) {
        System.out.println("STEP: Retrieving all crimes from empty repository");
        List<Crime> allCrimes = repository.readAll();
        
        System.out.println("STEP: Verifying result is an empty list");
        assertTrue(allCrimes.isEmpty(), "Result should be an empty list");
        System.out.println("Repository is empty as expected");
    }

    @Test
    public void testUpdate_withExistingCrime_shouldUpdateCrime(TestInfo testInfo) {
        System.out.println("STEP: Creating initial crime");
        Crime initialCrime = repository.create(testCrime);
        System.out.println("Initial crime created: " + initialCrime);
        System.out.println("  - Resolution status: " + (initialCrime.isResolved() ? "Resolved" : "Unresolved"));

        System.out.println("STEP: Creating updated version of the crime with resolution status = true");
        Crime updatedCrime = new Crime.Builder()
                .withId(testCrime.getId())
                .withDescription(testCrime.getDescription())
                .withLocation(testCrime.getLocation())
                .withCrimeType(testCrime.getCrimeType())
                .withReporterId(testCrime.getReporterId())
                .withReportedAt(testCrime.getReportedAt())
                .isResolved(true)
                .build();
        System.out.println("Updated crime created: " + updatedCrime);
        System.out.println("  - Resolution status: " + (updatedCrime.isResolved() ? "Resolved" : "Unresolved"));

        System.out.println("STEP: Updating crime in repository");
        Crime result = repository.update(updatedCrime);
        System.out.println("Update result: " + result);

        System.out.println("STEP: Verifying returned crime matches updated crime");
        assertEquals(updatedCrime, result, "Returned crime should match updated crime");
        
        System.out.println("STEP: Retrieving crime from repository to verify update");
        Optional<Crime> retrievedCrime = repository.read(testCrime.getId());
        System.out.println("STEP: Verifying crime was found");
        assertTrue(retrievedCrime.isPresent(), "Crime should be found in repository");
        System.out.println("  - Retrieved crime resolution status: " + 
                (retrievedCrime.get().isResolved() ? "Resolved" : "Unresolved"));
        System.out.println("STEP: Verifying crime is now resolved");
        assertTrue(retrievedCrime.get().isResolved(), "Crime should be marked as resolved");
    }

    @Test
    public void testUpdate_withNonExistingCrime_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Creating a crime that doesn't exist in the repository");
        Crime nonExistingCrime = CrimeFactory.createCrime(
                "Non-existing crime",
                "Unknown location",
                CrimeType.OTHER,
                "unknown"
        );
        System.out.println("Non-existing crime created: " + nonExistingCrime);
        System.out.println("ID: " + nonExistingCrime.getId());

        System.out.println("STEP: Attempting to update non-existing crime (should throw exception)");
        try {
            repository.update(nonExistingCrime);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate_withNullCrime_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to update a null crime (should throw exception)");
        try {
            repository.update(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDelete_withExistingId_shouldRemoveCrime(TestInfo testInfo) {
        System.out.println("STEP: Creating a crime to be deleted");
        Crime savedCrime = repository.create(testCrime);
        System.out.println("Crime created with ID: " + savedCrime.getId());

        System.out.println("STEP: Verifying repository contains one crime before deletion");
        assertEquals(1, repository.readAll().size(), "Repository should contain one crime");

        System.out.println("STEP: Deleting crime with ID: " + testCrime.getId());
        boolean result = repository.delete(testCrime.getId());

        System.out.println("STEP: Verifying deletion was successful");
        assertTrue(result, "Delete operation should return true");
        
        System.out.println("STEP: Verifying repository is now empty");
        assertTrue(repository.readAll().isEmpty(), "Repository should be empty after deletion");
        System.out.println("Repository is empty as expected");
    }

    @Test
    public void testDelete_withNonExistingId_shouldReturnFalse(TestInfo testInfo) {
        String nonExistingId = "non-existing-id";
        System.out.println("STEP: Attempting to delete a non-existent crime with ID: " + nonExistingId);
        
        boolean result = repository.delete(nonExistingId);
        
        System.out.println("STEP: Verifying deletion result is false");
        assertFalse(result, "Delete operation should return false for non-existent crime");
        System.out.println("Delete operation returned false as expected");
    }

    @Test
    public void testDelete_withNullId_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to delete a crime with null ID (should throw exception)");
        
        try {
            repository.delete(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindByCrimeType_withMatchingCrimes_shouldReturnFilteredList(TestInfo testInfo) {
        System.out.println("STEP: Creating a THEFT crime");
        Crime theftCrime = repository.create(testCrime);
        System.out.println("Created THEFT crime: " + theftCrime);

        System.out.println("STEP: Creating a FRAUD crime");
        Crime fraudCrime = CrimeFactory.createCrime(
                "Credit card fraud",
                "Online",
                CrimeType.FRAUD,
                "victim789"
        );
        repository.create(fraudCrime);
        System.out.println("Created FRAUD crime: " + fraudCrime);

        System.out.println("STEP: Creating another THEFT crime");
        Crime anotherTheftCrime = CrimeFactory.createCrime(
                "Phone stolen",
                "Bus station",
                CrimeType.THEFT,
                "victim456"
        );
        repository.create(anotherTheftCrime);
        System.out.println("Created another THEFT crime: " + anotherTheftCrime);

        System.out.println("STEP: Finding all crimes of type THEFT");
        List<Crime> theftCrimes = repository.findByCrimeType(CrimeType.THEFT);
        System.out.println("Found " + theftCrimes.size() + " THEFT crimes");

        System.out.println("STEP: Verifying correct number of THEFT crimes found");
        assertEquals(2, theftCrimes.size(), "Should find exactly 2 theft crimes");
        
        System.out.println("STEP: Verifying first theft crime is in result list");
        assertTrue(theftCrimes.contains(testCrime), "First theft crime should be in result list");
        
        System.out.println("STEP: Verifying second theft crime is in result list");
        assertTrue(theftCrimes.contains(anotherTheftCrime), "Second theft crime should be in result list");
        
        System.out.println("STEP: Verifying fraud crime is NOT in result list");
        assertFalse(theftCrimes.contains(fraudCrime), "Fraud crime should not be in result list");
    }

    @Test
    public void testFindByCrimeType_withNoMatchingCrimes_shouldReturnEmptyList(TestInfo testInfo) {
        System.out.println("STEP: Creating a THEFT crime");
        Crime savedCrime = repository.create(testCrime);
        System.out.println("Created THEFT crime: " + savedCrime);

        System.out.println("STEP: Finding all crimes of type ASSAULT (should be none)");
        List<Crime> assaultCrimes = repository.findByCrimeType(CrimeType.ASSAULT);
        
        System.out.println("STEP: Verifying result is an empty list");
        assertTrue(assaultCrimes.isEmpty(), "Result should be an empty list");
        System.out.println("Found 0 ASSAULT crimes as expected");
    }

    @Test
    public void testFindByCrimeType_withNullType_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to find crimes with null crime type (should throw exception)");
        
        try {
            repository.findByCrimeType(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindByLocation_withMatchingCrimes_shouldReturnFilteredList(TestInfo testInfo) {
        System.out.println("STEP: Creating first crime at 'City park'");
        Crime cityParkCrime1 = repository.create(testCrime);
        System.out.println("Created crime at '" + cityParkCrime1.getLocation() + "': " + cityParkCrime1);

        System.out.println("STEP: Creating second crime at 'Downtown parking lot'");
        Crime parkingLotCrime = CrimeFactory.createCrime(
                "Car break-in",
                "Downtown parking lot",
                CrimeType.BURGLARY,
                "victim123"
        );
        repository.create(parkingLotCrime);
        System.out.println("Created crime at '" + parkingLotCrime.getLocation() + "': " + parkingLotCrime);

        System.out.println("STEP: Creating third crime at 'City park at night'");
        Crime cityParkCrime2 = CrimeFactory.createCrime(
                "Assault incident",
                "City park at night",
                CrimeType.ASSAULT,
                "victim456"
        );
        repository.create(cityParkCrime2);
        System.out.println("Created crime at '" + cityParkCrime2.getLocation() + "': " + cityParkCrime2);

        System.out.println("STEP: Finding all crimes with location containing 'park'");
        List<Crime> parkCrimes = repository.findByLocation("park");
        System.out.println("Found " + parkCrimes.size() + " crimes containing 'park' in location");

        System.out.println("STEP: Verifying correct number of crimes found");
        assertEquals(3, parkCrimes.size(), "Should find exactly 3 crimes with 'park' in location");
        
        System.out.println("STEP: Verifying first crime is in result list");
        assertTrue(parkCrimes.contains(testCrime), "City park crime should be in result list");
        
        System.out.println("STEP: Verifying second crime is in result list");
        assertTrue(parkCrimes.contains(cityParkCrime2), "City park at night crime should be in result list");
        
        System.out.println("STEP: Verifying third crime is in result list");
        assertTrue(parkCrimes.contains(parkingLotCrime), "Downtown parking lot crime should be in result list");
    }

    @Test
    public void testFindByLocation_withNullLocation_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to find crimes with null location (should throw exception)");
        
        try {
            repository.findByLocation(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindByReporterId_withMatchingCrimes_shouldReturnFilteredList(TestInfo testInfo) {
        System.out.println("STEP: Creating first crime by reporter 'citizen123'");
        Crime reporter1Crime1 = repository.create(testCrime);
        System.out.println("Created crime by reporter '" + reporter1Crime1.getReporterId() + "': " + reporter1Crime1);

        System.out.println("STEP: Creating second crime by reporter 'citizen123'");
        Crime reporter1Crime2 = CrimeFactory.createCrime(
                "Vandalism",
                "Public library",
                CrimeType.VANDALISM,
                "citizen123" 
        );
        repository.create(reporter1Crime2);
        System.out.println("Created another crime by reporter '" + reporter1Crime2.getReporterId() + "': " + reporter1Crime2);

        System.out.println("STEP: Creating third crime by different reporter 'citizen456'");
        Crime reporter2Crime = CrimeFactory.createCrime(
                "Robbery",
                "Gas station",
                CrimeType.ROBBERY,
                "citizen456" 
        );
        repository.create(reporter2Crime);
        System.out.println("Created crime by reporter '" + reporter2Crime.getReporterId() + "': " + reporter2Crime);

        System.out.println("STEP: Finding all crimes by reporter 'citizen123'");
        List<Crime> citizen123Crimes = repository.findByReporterId("citizen123");
        System.out.println("Found " + citizen123Crimes.size() + " crimes by reporter 'citizen123'");

        System.out.println("STEP: Verifying correct number of crimes found");
        assertEquals(2, citizen123Crimes.size(), "Should find exactly 2 crimes by reporter 'citizen123'");
        
        System.out.println("STEP: Verifying first crime is in result list");
        assertTrue(citizen123Crimes.contains(testCrime), "First crime by citizen123 should be in result list");
        
        System.out.println("STEP: Verifying second crime is in result list");
        assertTrue(citizen123Crimes.contains(reporter1Crime2), "Second crime by citizen123 should be in result list");
        
        System.out.println("STEP: Verifying crime by different reporter is NOT in result list");
        assertFalse(citizen123Crimes.contains(reporter2Crime), "Crime by citizen456 should not be in result list");
    }

    @Test
    public void testFindByReporterId_withNullReporterId_shouldThrowException(TestInfo testInfo) {
        System.out.println("STEP: Attempting to find crimes with null reporter ID (should throw exception)");
        
        try {
            repository.findByReporterId(null);
            System.out.println("ERROR: Exception was not thrown");
            fail("Expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED: Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindByResolutionStatus_withMatchingCrimes_shouldReturnFilteredList(TestInfo testInfo) {
        System.out.println("STEP: Creating unresolved crime");
        Crime unresolvedCrime1 = repository.create(testCrime);
        System.out.println("Created unresolved crime: " + unresolvedCrime1);
        System.out.println("  - Resolution status: " + (unresolvedCrime1.isResolved() ? "Resolved" : "Unresolved"));

        System.out.println("STEP: Creating resolved crime");
        Crime resolvedCrime = CrimeFactory.createResolvedCrime(
                "Resolved theft",
                "Mall",
                CrimeType.THEFT,
                "securityGuard123"
        );
        repository.create(resolvedCrime);
        System.out.println("Created resolved crime: " + resolvedCrime);
        System.out.println("  - Resolution status: " + (resolvedCrime.isResolved() ? "Resolved" : "Unresolved"));

        System.out.println("STEP: Creating another unresolved crime");
        Crime unresolvedCrime2 = CrimeFactory.createCrime(
                "Another unresolved crime",
                "School",
                CrimeType.VANDALISM,
                "principal456"
        );
        repository.create(unresolvedCrime2);
        System.out.println("Created another unresolved crime: " + unresolvedCrime2);
        System.out.println("  - Resolution status: " + (unresolvedCrime2.isResolved() ? "Resolved" : "Unresolved"));

        System.out.println("STEP: Finding all resolved crimes");
        List<Crime> resolvedCrimes = repository.findByResolutionStatus(true);
        System.out.println("Found " + resolvedCrimes.size() + " resolved crimes");

        System.out.println("STEP: Verifying correct number of resolved crimes found");
        assertEquals(1, resolvedCrimes.size(), "Should find exactly 1 resolved crime");
        
        System.out.println("STEP: Verifying resolved crime is in result list");
        assertTrue(resolvedCrimes.contains(resolvedCrime), "Resolved crime should be in result list");

        System.out.println("STEP: Finding all unresolved crimes");
        List<Crime> unresolvedCrimes = repository.findByResolutionStatus(false);
        System.out.println("Found " + unresolvedCrimes.size() + " unresolved crimes");

        System.out.println("STEP: Verifying correct number of unresolved crimes found");
        assertEquals(2, unresolvedCrimes.size(), "Should find exactly 2 unresolved crimes");
        
        System.out.println("STEP: Verifying first unresolved crime is in result list");
        assertTrue(unresolvedCrimes.contains(testCrime), "First unresolved crime should be in result list");
        
        System.out.println("STEP: Verifying second unresolved crime is in result list");
        assertTrue(unresolvedCrimes.contains(unresolvedCrime2), "Second unresolved crime should be in result list");
    }
}
