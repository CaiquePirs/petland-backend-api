package com.petland.common.auth;

import com.petland.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AccessValidator {

    private final HttpServletRequest request;

    public UUID getLoggedInUser() {
        Object userIdAttr = request.getAttribute("id");

        if (userIdAttr == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return UUID.fromString(userIdAttr.toString());
    }
}
