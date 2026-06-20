package com.library.management_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        String author,
        String genre,
        String description,
        @JsonProperty("copy_count") int copyCount
) {}
