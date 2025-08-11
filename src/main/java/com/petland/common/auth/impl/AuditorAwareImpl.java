package com.petland.common.auth.impl;

import com.petland.common.auth.validator.AccessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final AccessValidator accessValidator;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        UUID userId = accessValidator.getLoggedInUser();
        return Optional.ofNullable(userId);
    }
}
