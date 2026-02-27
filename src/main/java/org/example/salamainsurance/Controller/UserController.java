package org.example.salamainsurance.Controller;

import jakarta.validation.Valid;
import org.example.salamainsurance.DTO.UserCreateRequest;
import org.example.salamainsurance.DTO.UserResponse;
import org.example.salamainsurance.DTO.UserUpdateRequest;
import org.example.salamainsurance.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ GET /api/users/me : profil du user connecté
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // email depuis le JWT (subject)
        UserResponse response = userService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    // ✅ POST /api/users : ADMIN uniquement
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ GET /api/users : ADMIN uniquement
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> responses = userService.getAll();
        return ResponseEntity.ok(responses);
    }

    // ✅ GET /api/users/{id} : ADMIN OU user lui-même
    // IMPORTANT: {id:\\d+} empêche /me d'être interprété comme un id
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or @rbacSecurity.hasUserId(#id, authentication)")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id, Authentication authentication) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    // ✅ GET /api/users/by-email : ADMIN uniquement
    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getByEmail(@RequestParam String email) {
        UserResponse response = userService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    // ✅ PUT /api/users/{id} : ADMIN OU user lui-même
    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or @rbacSecurity.hasUserId(#id, authentication)")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody UserUpdateRequest request,
                                               Authentication authentication) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // ✅ DELETE /api/users/{id} : ADMIN uniquement
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}