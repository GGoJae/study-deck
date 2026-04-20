package org.example.filestore.shared.model;

public record Counters(
        long nextCategoryId,
        long nextSubCategoryId,
        long nextCardId
) {
    public Counters increaseCategoryId() {
        return new Counters(
                this.nextCategoryId + 1,
                this.nextSubCategoryId,
                this.nextCardId
        );
    }
}
