package org.example.cli.model.display;

import org.example.filestore.shared.model.Type;

import java.util.Objects;
import java.util.function.Function;

public record Status(
        String categoryName,
        String subCategoryName,
        String contentType,
        String contentName

) {

    public String forDisplay() {
        return """
                
                [ category    ]    %s
                [ subCategory ]    %s
                [ content     ]  [ %s ]  %s
                
                """.formatted(
                categoryName == null ? "" : categoryName,
                subCategoryName == null ? "" : subCategoryName,
                contentType == null ? "" : contentType,
                contentName == null ? "" : contentName
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String categoryName;
        private String subCategoryName;
        private String contentType;
        private String contentName;

        public Builder categoryName(Long categoryId, Function<Long, String> getDisplayNameFunction) {
            if (Objects.isNull(categoryId)) {
                this.categoryName = null;
                return this;
            }

            this.categoryName = getDisplayNameFunction.apply(categoryId);
            return this;
        }

        public Builder subCategoryName(Long subCategoryId, Function<Long, String> getDisplayNameFunction) {
            if (Objects.isNull(subCategoryId)) {
                this.subCategoryName = null;
                return this;
            }

            this.subCategoryName = getDisplayNameFunction.apply(subCategoryId);
            return this;
        }

        public Builder content(Type type, Long contentId, Function<Long, String> getDisplayNameFunction) {
            if (Objects.isNull(contentId)) {
                this.contentType = null;
                this.contentName = null;
                return this;
            }

            Objects.requireNonNull(type);
            this.contentType = type.name();
            this.contentName = getDisplayNameFunction.apply(contentId);
            return this;
        }

        public Status build() {
            return new Status(this.categoryName, this.subCategoryName, this.contentType, this.contentName);
        }
    }

}
