package com.library.management_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.management_system.model.type.LendingType;

import java.util.UUID;

public record BookRequestCreationDTO(
        @JsonProperty("book_id") UUID bookId,
        @JsonProperty("lending_type") LendingType lendingType
) {}