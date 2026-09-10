package codinpad.springboot.repository;

import codinpad.springboot.entity.LeadAssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadAssignmentHistoryRepository
        extends JpaRepository<LeadAssignmentHistory, Long> {

    List<LeadAssignmentHistory>
        findByLeadIdOrderByAssignedAtAsc(Long leadId);
}
