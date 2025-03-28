package za.ac.cput;

/* CrimeReportingApp.java
Author: Zakhir McKinnon (222016299)
Date: 28 March 2025
*/

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;
import za.ac.cput.factory.CrimeFactory;
import za.ac.cput.repository.CrimeRepository;
import za.ac.cput.repository.impl.CrimeRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CrimeReportingApp {

    private static final CrimeRepository crimeRepository = CrimeRepositoryImpl.getRepository();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Crime Reporting System");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    reportCrime();
                    break;
                case 2:
                    viewAllCrimes();
                    break;
                case 3:
                    searchCrimeById();
                    break;
                case 4:
                    searchCrimesByType();
                    break;
                case 5:
                    searchCrimesByLocation();
                    break;
                case 6:
                    updateCrimeStatus();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using the Crime Reporting System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1. Report a Crime");
        System.out.println("2. View All Crimes");
        System.out.println("3. Search Crime by ID");
        System.out.println("4. Search Crimes by Type");
        System.out.println("5. Search Crimes by Location");
        System.out.println("6. Update Crime Resolution Status");
        System.out.println("7. Exit");
        System.out.println("================");
    }
    
    private static void reportCrime() {
        System.out.println("\n--- Report a Crime ---");
        
        String description = getStringInput("Enter crime description: ");
        String location = getStringInput("Enter crime location: ");
        
        System.out.println("Crime Types:");
        CrimeType[] crimeTypes = CrimeType.values();
        for (int i = 0; i < crimeTypes.length; i++) {
            System.out.println((i + 1) + ". " + crimeTypes[i].getDisplayName());
        }
        
        int typeChoice = getIntInput("Select crime type (1-" + crimeTypes.length + "): ");
        if (typeChoice < 1 || typeChoice > crimeTypes.length) {
            System.out.println("Invalid choice. Defaulting to OTHER.");
            typeChoice = crimeTypes.length;
        }
        
        CrimeType selectedType = crimeTypes[typeChoice - 1];
        String reporterId = getStringInput("Enter your ID (for reporting purposes): ");
        
        Crime crime = CrimeFactory.createCrime(description, location, selectedType, reporterId);
        crimeRepository.create(crime);
        
        System.out.println("Crime reported successfully. Crime ID: " + crime.getId());
    }
    
    private static void viewAllCrimes() {
        System.out.println("\n--- All Crimes ---");
        List<Crime> allCrimes = crimeRepository.readAll();
        
        if (allCrimes.isEmpty()) {
            System.out.println("No crimes have been reported yet.");
            return;
        }
        
        displayCrimeList(allCrimes);
    }
    
    private static void searchCrimeById() {
        System.out.println("\n--- Search Crime by ID ---");
        String id = getStringInput("Enter crime ID: ");
        
        Optional<Crime> crimeOpt = crimeRepository.read(id);
        if (crimeOpt.isPresent()) {
            displayCrime(crimeOpt.get());
        } else {
            System.out.println("No crime found with ID: " + id);
        }
    }
    
    private static void searchCrimesByType() {
        System.out.println("\n--- Search Crimes by Type ---");
        
        System.out.println("Crime Types:");
        CrimeType[] crimeTypes = CrimeType.values();
        for (int i = 0; i < crimeTypes.length; i++) {
            System.out.println((i + 1) + ". " + crimeTypes[i].getDisplayName());
        }
        
        int typeChoice = getIntInput("Select crime type (1-" + crimeTypes.length + "): ");
        if (typeChoice < 1 || typeChoice > crimeTypes.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        CrimeType selectedType = crimeTypes[typeChoice - 1];
        List<Crime> crimes = crimeRepository.findByCrimeType(selectedType);
        
        if (crimes.isEmpty()) {
            System.out.println("No crimes found of type: " + selectedType.getDisplayName());
            return;
        }
        
        System.out.println("Crimes of type " + selectedType.getDisplayName() + ":");
        displayCrimeList(crimes);
    }
    
    private static void searchCrimesByLocation() {
        System.out.println("\n--- Search Crimes by Location ---");
        String location = getStringInput("Enter location to search for: ");
        
        List<Crime> crimes = crimeRepository.findByLocation(location);
        
        if (crimes.isEmpty()) {
            System.out.println("No crimes found at location containing: " + location);
            return;
        }
        
        System.out.println("Crimes at location containing '" + location + "':");
        displayCrimeList(crimes);
    }
    
    private static void updateCrimeStatus() {
        System.out.println("\n--- Update Crime Resolution Status ---");
        String id = getStringInput("Enter crime ID: ");
        
        Optional<Crime> crimeOpt = crimeRepository.read(id);
        if (!crimeOpt.isPresent()) {
            System.out.println("No crime found with ID: " + id);
            return;
        }
        
        Crime crime = crimeOpt.get();
        System.out.println("Current crime details:");
        displayCrime(crime);
        
        String input = getStringInput("Mark as resolved? (yes/no): ");
        boolean isResolved = input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y");
        
        Crime updatedCrime = new Crime.Builder()
                .withId(crime.getId())
                .withDescription(crime.getDescription())
                .withLocation(crime.getLocation())
                .withReportedAt(crime.getReportedAt())
                .withCrimeType(crime.getCrimeType())
                .withReporterId(crime.getReporterId())
                .isResolved(isResolved)
                .build();
        
        crimeRepository.update(updatedCrime);
        System.out.println("Crime status updated successfully.");
    }
    
    private static void displayCrimeList(List<Crime> crimes) {
        System.out.println("Total crimes: " + crimes.size());
        for (int i = 0; i < crimes.size(); i++) {
            System.out.println("\nCrime #" + (i + 1) + ":");
            displayCrime(crimes.get(i));
        }
    }
    
    private static void displayCrime(Crime crime) {
        System.out.println("ID: " + crime.getId());
        System.out.println("Type: " + crime.getCrimeType().getDisplayName());
        System.out.println("Description: " + crime.getDescription());
        System.out.println("Location: " + crime.getLocation());
        System.out.println("Reported At: " + crime.getReportedAt());
        System.out.println("Reporter ID: " + crime.getReporterId());
        System.out.println("Status: " + (crime.isResolved() ? "Resolved" : "Unresolved"));
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
