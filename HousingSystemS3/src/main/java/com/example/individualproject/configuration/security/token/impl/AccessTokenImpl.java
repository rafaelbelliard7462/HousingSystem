package com.example.individualproject.configuration.security.token.impl;

import com.example.individualproject.configuration.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final Set<String> roles;

    public AccessTokenImpl(String subject, Long studentId, Collection<String> roles) {
        this.subject = subject;
        this.userId = studentId;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();

    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
