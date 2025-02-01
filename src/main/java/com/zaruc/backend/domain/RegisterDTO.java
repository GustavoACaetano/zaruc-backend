package com.zaruc.backend.domain;

import com.zaruc.backend.domain.user.UserRole;

public record RegisterDTO(String login, String name, String password, UserRole role) {
}