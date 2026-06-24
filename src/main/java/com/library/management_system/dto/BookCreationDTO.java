package com.library.management_system.dto;

import jakarta.validation.constraints.NotBlank;

public record BookCreationDTO(
        @NotBlank(message = "Username must not be blank") String title,
        @NotBlank(message = "Author must not be blank") String author,
        @NotBlank(message = "Genre must not be blank") String genre,
        String description
) {}