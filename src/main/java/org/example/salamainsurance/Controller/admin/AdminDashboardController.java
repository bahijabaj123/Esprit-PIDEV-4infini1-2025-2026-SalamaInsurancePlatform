package org.example.salamainsurance.Controller.admin;

import org.example.salamainsurance.DTO.admin.LabelCountDto;
import org.example.salamainsurance.DTO.admin.UserSummaryDto;
import org.example.salamainsurance.Service.admin.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/user-summary")
    public ResponseEntity<UserSummaryDto> userSummary() {
        return ResponseEntity.ok(adminDashboardService.userSummary());
    }

    @GetMapping("/users-by-role")
    public ResponseEntity<List<LabelCountDto>> usersByRole() {
        return ResponseEntity.ok(adminDashboardService.usersByRole());
    }

    @GetMapping("/users-by-approval-status")
    public ResponseEntity<List<LabelCountDto>> usersByApprovalStatus() {
        return ResponseEntity.ok(adminDashboardService.usersByApprovalStatus());
    }
}

