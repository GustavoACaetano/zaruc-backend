package com.zaruc.backend.domain.user;

public record UserDTO(String id, String login, String name, String password, UserRole role) {
}