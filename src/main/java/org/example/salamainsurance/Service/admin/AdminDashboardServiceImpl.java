package org.example.salamainsurance.Service.admin;

import org.example.salamainsurance.DTO.admin.AdminUserRowDto;
import org.example.salamainsurance.DTO.admin.LabelCountDto;
import org.example.salamainsurance.DTO.admin.PageResponse;
import org.example.salamainsurance.DTO.admin.UserSummaryDto;
import org.example.salamainsurance.Entity.ApprovalStatus;
import org.example.salamainsurance.Entity.RoleName;
import org.example.salamainsurance.Entity.User;
import org.example.salamainsurance.Repository.UserRepository;
import org.example.salamainsurance.Repository.spec.UserAdminSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;

    public AdminDashboardServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdminUserRowDto> listUsers(
            int page,
            int size,
            String sort,
            String search,
            RoleName role,
            RoleName requestedRole,
            ApprovalStatus approvalStatus,
            Boolean enabled,
            Boolean locked
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                parseSort(sort)
        );

        Specification<User> spec = UserAdminSpecifications.adminFilters(
                search, role, requestedRole, approvalStatus, enabled, locked
        );

        Page<User> result = userRepository.findAll(spec, pageable);

        List<AdminUserRowDto> content = result.getContent().stream()
                .map(this::toRowDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserSummaryDto userSummary() {
        UserSummaryDto dto = new UserSummaryDto();

        dto.setTotalUsers(userRepository.count());
        dto.setTotalClients(userRepository.countByRole(RoleName.CLIENT));
        dto.setTotalAssureurs(userRepository.countByRole(RoleName.ASSUREUR));
        dto.setTotalExperts(userRepository.countByRole(RoleName.EXPERT));
        dto.setTotalAdmins(userRepository.countByRole(RoleName.ADMIN));

        dto.setPendingApprovals(userRepository.countByApprovalStatus(ApprovalStatus.PENDING));
        dto.setRejectedRequests(userRepository.countByApprovalStatus(ApprovalStatus.REJECTED));

        dto.setLockedUsers(userRepository.countByLockedTrue());
        dto.setEnabledUsers(userRepository.countByEnabledTrue());
        dto.setDisabledUsers(userRepository.countByEnabledFalse());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelCountDto> usersByRole() {
        return userRepository.countUsersByRole().stream()
                .map(v -> new LabelCountDto(v.getLabel(), v.getCount()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelCountDto> usersByApprovalStatus() {
        return userRepository.countUsersByApprovalStatus().stream()
                .map(v -> new LabelCountDto(v.getLabel(), v.getCount()))
                .collect(Collectors.toList());
    }

    private AdminUserRowDto toRowDto(User u) {
        AdminUserRowDto dto = new AdminUserRowDto();
        dto.setId(u.getId());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setRequestedRole(u.getRequestedRole());
        dto.setApprovalStatus(u.getApprovalStatus());
        dto.setEnabled(u.isEnabled());
        dto.setLocked(u.isLocked());
        dto.setCreatedAt(u.getCreatedAt());
        dto.setUpdatedAt(u.getUpdatedAt());
        return dto;
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length > 1) {
            direction = Sort.Direction.fromOptionalString(parts[1].trim()).orElse(Sort.Direction.DESC);
        }
        return Sort.by(direction, property);
    }
}

