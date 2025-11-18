package com.dev.pernambox.domain.file.dtos;

import java.util.List;

public record FileResponseDto(int quantity, List<String> fileNames) {
}
