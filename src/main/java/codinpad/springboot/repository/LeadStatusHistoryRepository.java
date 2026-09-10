package codinpad.springboot.repository;

import codinpad.springboot.entity.LeadStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadStatusHistoryRepository
        extends JpaRepository<LeadStatusHistory, Long> {

    List<LeadStatusHistory>
        findByLeadIdOrderByChangedAtAsc(Long leadId);
}
