package com.petland.common.auth;

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
            return null;
        }
        return UUID.fromString(userIdAttr.toString());
    }
}
