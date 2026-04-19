package org.example.filestore.shared.model;

public record Counters(
        long nextCategoryId,
        long nextSubCategoryId,
        long nextCardId
) {
}
