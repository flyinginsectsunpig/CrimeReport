package za.ac.cput.repository.impl;

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;
import za.ac.cput.repository.CrimeRepository;

import java.util.*;
import java.util.stream.Collectors;

public class CrimeRepositoryImpl implements CrimeRepository {
    
    private static CrimeRepositoryImpl repository = null;
    private List<Crime> crimeList = new ArrayList<Crime>();
    
    private CrimeRepositoryImpl() {
    }
    
    public static CrimeRepositoryImpl getRepository() {
        if (repository == null) {
            repository = new CrimeRepositoryImpl();
        }
        return repository;
    }
    
    // Method for testing purposes
    public void clearRepository() {
        crimeList.clear();
    }
    
    @Override
    public Crime create(Crime crime) {
        if (crime == null) {
            throw new IllegalArgumentException("Crime cannot be null");
        }
        
        for (Crime c : crimeList) {
            if (c.getId().equals(crime.getId())) {
                throw new IllegalArgumentException("Crime with ID " + crime.getId() + " already exists");
            }
        }
        
        this.crimeList.add(crime);
        return crime;
    }
    
    @Override
    public Optional<Crime> read(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        for (Crime c : crimeList) {
            if (c.getId().equals(id)) return Optional.of(c);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Crime> readAll() {
        return this.crimeList;
    }
    
    @Override
    public Crime update(Crime crime) {
        if (crime == null) {
            throw new IllegalArgumentException("Crime cannot be null");
        }
        
        Crime toUpdate = read(crime.getId()).orElse(null);
        if (toUpdate != null) {
            crimeList.remove(toUpdate);
            crimeList.add(crime);
            return crime;
        }
        throw new IllegalArgumentException("Crime with ID " + crime.getId() + " does not exist");
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        Crime crime = read(id).orElse(null);
        if (crime != null) {
            crimeList.remove(crime);
            return true;
        }
        return false;
    }
    
    @Override
    public List<Crime> findByCrimeType(CrimeType crimeType) {
        if (crimeType == null) {
            throw new IllegalArgumentException("Crime type cannot be null");
        }
        
        List<Crime> crimesOfType = new ArrayList<Crime>();
        for (Crime c : crimeList) {
            if (c.getCrimeType() == crimeType) crimesOfType.add(c);
        }
        return crimesOfType;
    }
    
    @Override
    public List<Crime> findByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        
        List<Crime> crimesAtLocation = new ArrayList<Crime>();
        for (Crime c : crimeList) {
            if (c.getLocation().toLowerCase().contains(location.toLowerCase())) crimesAtLocation.add(c);
        }
        return crimesAtLocation;
    }
    
    @Override
    public List<Crime> findByReporterId(String reporterId) {
        if (reporterId == null || reporterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Reporter ID cannot be null or empty");
        }
        
        List<Crime> crimesByReporter = new ArrayList<Crime>();
        for (Crime c : crimeList) {
            if (c.getReporterId().equals(reporterId)) crimesByReporter.add(c);
        }
        return crimesByReporter;
    }
    
    @Override
    public List<Crime> findByResolutionStatus(boolean isResolved) {
        List<Crime> crimesByStatus = new ArrayList<Crime>();
        for (Crime c : crimeList) {
            if (c.isResolved() == isResolved) crimesByStatus.add(c);
        }
        return crimesByStatus;
    }
}
