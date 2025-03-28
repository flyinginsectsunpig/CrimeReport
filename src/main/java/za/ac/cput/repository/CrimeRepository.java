package za.ac.cput.repository;

import za.ac.cput.domain.Crime;
import za.ac.cput.domain.CrimeType;

import java.util.List;

public interface CrimeRepository extends IRepository<Crime, String> {
    
    List<Crime> findByCrimeType(CrimeType crimeType);
    
    List<Crime> findByLocation(String location);
    
    List<Crime> findByReporterId(String reporterId);
    
    List<Crime> findByResolutionStatus(boolean isResolved);
}
