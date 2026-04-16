package org.example.salamainsurance.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.salamainsurance.Entity.ApprovalStatus;
import org.example.salamainsurance.Entity.RoleName;
import org.example.salamainsurance.Entity.User;
import org.example.salamainsurance.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${oauth2.success-redirect-url:http://localhost:4200/oauth2/success}")
    private String successRedirectUrl;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository,
                                             JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .fullName(name != null ? name : email)
                            .role(RoleName.CLIENT)
                            .requestedRole(null)
                            .approvalStatus(ApprovalStatus.APPROVED)
                            .enabled(true)
                            .locked(false)
                            .failedLoginAttempts(0)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user.getEmail());

        clearAuthenticationAttributes(request);

        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String redirectUrl = successRedirectUrl + (successRedirectUrl.contains("?") ? "&" : "?") + "token=" + encodedToken;
        response.sendRedirect(redirectUrl);
    }
}
