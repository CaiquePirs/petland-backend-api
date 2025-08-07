package com.petland.common.auth.validator;

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
            return null;
        }
        return UUID.fromString(userIdAttr.toString());
    }

    public void isOwnerOrAdmin(UUID ownerId){
        boolean isAdmin = request.isUserInRole("ADMIN");
        boolean isOwner = request.getAttribute("id").toString().equals(ownerId.toString());

        if(!isAdmin && !isOwner){
            throw new UnauthorizedException("User not authorized");
        }
    }
}
