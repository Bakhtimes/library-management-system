package com.library.management_system.dto;

public record BookCreationDTO(
        String title,
        String author,
        String genre,
        String description
) {}