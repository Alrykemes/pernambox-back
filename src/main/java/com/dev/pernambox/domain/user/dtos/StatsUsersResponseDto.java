package com.dev.pernambox.domain.user.dtos;

public record StatsUsersResponseDto(Long total, Long actives, Long deactiveds, Long admins) {}