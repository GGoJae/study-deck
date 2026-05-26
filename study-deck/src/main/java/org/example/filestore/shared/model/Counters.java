package org.example.filestore.shared.model;

public record Counters(
        long nextCategoryId,
        long nextSubCategoryId,
        long nextCardId,
        long nextAnswerId
) {
    public Counters increaseCategoryId() {
        return new Counters(
                this.nextCategoryId + 1,
                this.nextSubCategoryId,
                this.nextCardId,
                this.nextAnswerId
        );
    }

    public Counters increaseSubCategoryId() {
        return new Counters(
                this.nextCategoryId,
                this.nextSubCategoryId + 1,
                this.nextCardId,
                this.nextAnswerId
        );
    }

    public Counters increaseCardId() {
        return new Counters(
                this.nextCategoryId,
                this.nextSubCategoryId,
                this.nextCardId + 1,
                this.nextAnswerId
        );
    }

    public Counters increaseAnswerId() {
        return new Counters(
                this.nextCategoryId,
                this.nextSubCategoryId,
                this.nextCardId,
                this.nextAnswerId + 1
        );
    }
}
