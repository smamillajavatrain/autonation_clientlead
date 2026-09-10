package codinpad.springboot.repository;

import codinpad.springboot.entity.CustomerLead;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerLeadRepository
        extends JpaRepository<CustomerLead, Long>,
                JpaSpecificationExecutor<CustomerLead> {

    Optional<CustomerLead> findByIdempotencyKey(String idempotencyKey);

    Optional<CustomerLead> findFirstByEmailIgnoreCaseAndPhone(
            String email, String phone);
}
