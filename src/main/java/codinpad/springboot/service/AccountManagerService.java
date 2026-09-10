package codinpad.springboot.service;

import codinpad.springboot.dto.request.AccountManagerRequest;
import codinpad.springboot.dto.response.AccountManagerResponse;

import java.util.List;

public interface AccountManagerService {

    AccountManagerResponse create(AccountManagerRequest request);

    AccountManagerResponse getById(Long id);

    List<AccountManagerResponse> getAll();
}
