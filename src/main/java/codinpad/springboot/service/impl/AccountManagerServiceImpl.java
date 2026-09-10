package codinpad.springboot.service.impl;

import codinpad.springboot.dto.request.AccountManagerRequest;
import codinpad.springboot.dto.response.AccountManagerResponse;
import codinpad.springboot.entity.AccountManager;
import codinpad.springboot.enums.ErrorCode;
import codinpad.springboot.exception.CrmBusinessException;
import codinpad.springboot.mapper.AccountManagerMapper;
import codinpad.springboot.repository.AccountManagerRepository;
import codinpad.springboot.service.AccountManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountManagerServiceImpl implements AccountManagerService {

    private final AccountManagerRepository repository;
    private final AccountManagerMapper mapper;

    @Override
    public AccountManagerResponse create(AccountManagerRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new CrmBusinessException(
                    ErrorCode.ACCOUNT_MANAGER_EMAIL_EXISTS,
                    "Account manager with email '" + email + "' already exists");
        }

        AccountManager manager = mapper.toEntity(request);
        manager.setEmail(email);

        return mapper.toResponse(repository.save(manager));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountManagerResponse getById(Long id) {
        return mapper.toResponse(findManager(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountManagerResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private AccountManager findManager(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CrmBusinessException(
                        ErrorCode.ACCOUNT_MANAGER_NOT_FOUND,
                        "Account manager not found: " + id));
    }
}
