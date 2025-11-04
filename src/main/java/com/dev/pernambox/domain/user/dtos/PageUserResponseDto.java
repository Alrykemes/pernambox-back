package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageUserResponseDto(int previousPage, int currentPage, int nextPage, int totalPages, int size, List<UserResponseDto> content) {
    public PageUserResponseDto (Page<User> pageUsers) {
        this(
                pageUsers.isFirst() ? 1 : pageUsers.getNumber(),
                pageUsers.getNumber() + 1,
                pageUsers.isLast() ? pageUsers.getNumber() + 1 : pageUsers.getNumber() + 2,
                pageUsers.getTotalPages(),
                pageUsers.getSize(),
                pageUsers.getContent().stream().map(UserResponseDto::new).toList()
        );
    }
}
