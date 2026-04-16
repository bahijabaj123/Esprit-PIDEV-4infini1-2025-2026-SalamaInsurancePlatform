package org.example.salamainsurance.Service.admin;

import org.example.salamainsurance.DTO.admin.AdminUserRowDto;
import org.example.salamainsurance.DTO.admin.LabelCountDto;
import org.example.salamainsurance.DTO.admin.PageResponse;
import org.example.salamainsurance.DTO.admin.UserSummaryDto;
import org.example.salamainsurance.Entity.ApprovalStatus;
import org.example.salamainsurance.Entity.RoleName;

import java.util.List;

public interface AdminDashboardService {

    PageResponse<AdminUserRowDto> listUsers(
            int page,
            int size,
            String sort,
            String search,
            RoleName role,
            RoleName requestedRole,
            ApprovalStatus approvalStatus,
            Boolean enabled,
            Boolean locked
    );

    UserSummaryDto userSummary();

    List<LabelCountDto> usersByRole();

    List<LabelCountDto> usersByApprovalStatus();
}

