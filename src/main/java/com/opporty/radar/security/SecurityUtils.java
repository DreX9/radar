package com.opporty.radar.security;

import com.opporty.radar.features.auth.users.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    /**
     * Gets the currently authenticated user from the SecurityContext.
     * @return Users entity or null if not authenticated.
     */
    public static Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getUser();
        }
        return null;
    }
}
