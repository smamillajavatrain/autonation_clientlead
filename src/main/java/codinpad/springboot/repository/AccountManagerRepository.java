package codinpad.springboot.repository;

import codinpad.springboot.entity.AccountManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountManagerRepository
        extends JpaRepository<AccountManager, Long> {

    Optional<AccountManager> findByEmailIgnoreCase(String email);
}
