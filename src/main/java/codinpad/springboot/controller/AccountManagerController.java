package codinpad.springboot.controller;

import codinpad.springboot.dto.request.AccountManagerRequest;
import codinpad.springboot.dto.response.AccountManagerResponse;
import codinpad.springboot.service.AccountManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/account-managers")
@RequiredArgsConstructor
public class AccountManagerController {

    private final AccountManagerService service;

    @PostMapping
    public ResponseEntity<AccountManagerResponse> create(
            @Valid @RequestBody AccountManagerRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountManagerResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountManagerResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
