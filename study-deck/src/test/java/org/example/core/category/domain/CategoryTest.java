package org.example.core.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CategoryTest {

    @Test
    @DisplayName("[생성자] ownerId 가 null 이면 IllegalArgumentException 이 터진다.")
    void when_ownerId_isNull_throw_IllegalArgumentException() {
        // given
        Long ownerId = null;

        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, ownerId, "name", 100, now, now, ownerId, ownerId);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ownerId는 필수입니다.");
    }

    @Test
    @DisplayName("[생성자] categoryName 이  null 이면 IllegalArgumentException 이 터진다.")
    void when_categoryName_isNull_throw_IllegalArgumentException() {
        // given
        String categoryName = null;

        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, 1L, categoryName, 100, now, now, 1L, 1L);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }


    @ParameterizedTest
    @ValueSource(strings = {" ", "    ", "\n", "\t"})
    @DisplayName("[생성자] categoryName 이 blank 이면 IllegalArgumentException 이 터진다.")
    void when_categoryName_isBlank_throw_IllegalArgumentException(String categoryName) {
        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, 1L, categoryName, 100, now, now, 1L, 1L);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("[생성자] createdAt 이  null 이면 IllegalArgumentException 이 터진다.")
    void when_createdAt_isNull_throw_IllegalArgumentException() {
        // given
        Instant createdAt = null;

        // when, then
        assertThatThrownBy(
                () -> new Category(null, 1L, "categoryName", 100, createdAt, createdAt, 1L, 1L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("생성 일자는 필수입니다.");
    }


    @Test
    @DisplayName("[생성자] updatedAt 이  null 이면 IllegalArgumentException 이 터진다.")
    void when_updatedAt_isNull_throw_IllegalArgumentException() {
        // given
        Instant updatedAt = null;

        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, 1L, "categoryName", 100, now, updatedAt, 1L, 1L);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수정 일자는 필수입니다.");
    }


    @Test
    @DisplayName("[생성자] createdUser 이  null 이면 IllegalArgumentException 이 터진다.")
    void when_createdUser_isNull_throw_IllegalArgumentException() {
        // given
        Long createdUser = null;

        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, 1L, "categoryName", 100, now, now, createdUser, 1L);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("생성자 id는 필수입니다.");
    }


    @Test
    @DisplayName("[생성자] updatedUser 이  null 이면 IllegalArgumentException 이 터진다.")
    void when_updatedUser_isNull_throw_IllegalArgumentException() {
        // given
        Long updatedUser = null;

        // when, then
        assertThatThrownBy(
                () -> {
                    Instant now = Instant.now();
                    new Category(null, 1L, "categoryName", 100, now, now, 1L, updatedUser);
                }
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수정자 id는 필수입니다.");
    }

    @Test
    @DisplayName("[withId] 원본은 변하지 않고 새객체가 생성된다.")
    void original_is_not_changed_create_new_instance() {
        // given
        Long userId = 1L;
        String categoryName = "categoryName";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(null, userId, categoryName, sortKey, now, now, userId, userId);
        Long categoryId = 1L;

        // when
        Category afterWithId = original.withId(categoryId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(original.getId())
                        .isNull(),
                () -> assertThat(original.getOwnerId())
                        .isEqualTo(userId),
                () -> assertThat(original.getName())
                        .isEqualTo(categoryName),
                () -> assertThat(original.getSortKey())
                        .isEqualTo(sortKey),
                () -> assertThat(original.getCreatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getUpdatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getCreatedUser())
                        .isEqualTo(userId),
                () -> assertThat(original.getUpdatedUser())
                        .isEqualTo(userId),
                () -> assertThat(afterWithId.getId())
                        .isEqualTo(categoryId)
        );

    }


    @Test
    @DisplayName("[withId] 새객체는 id외의 원본의 값들을 그대로 가져간다.")
    void new_instance_has_originals_same_values() {
        // given
        Long userId = 1L;
        String categoryName = "categoryName";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(null, userId, categoryName, sortKey, now, now, userId, userId);
        Long categoryId = 1L;

        // when
        Category afterWithId = original.withId(categoryId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(afterWithId.getOwnerId())
                        .isEqualTo(original.getOwnerId()),
                () -> assertThat(afterWithId.getName())
                        .isEqualTo(original.getName()),
                () -> assertThat(afterWithId.getSortKey())
                        .isEqualTo(original.getSortKey()),
                () -> assertThat(afterWithId.getCreatedAt())
                        .isEqualTo(original.getCreatedAt()),
                () -> assertThat(afterWithId.getCreatedUser())
                        .isEqualTo(original.getCreatedUser()),
                () -> assertThat(afterWithId.getId())
                        .isEqualTo(categoryId)
        );

    }


    @Test
    @DisplayName("[withId] id에 null을 넣으면 IllegalArgumentException이 터진다.")
    void when_withIds_parameter_input_null_should_throw_IllegalArgumentException() {
        // given
        Long userId = 1L;
        String categoryName = "categoryName";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(null, userId, categoryName, sortKey, now, now, userId, userId);
        Long categoryId = null;

        // when, then
        assertThatThrownBy(() -> original.withId(categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id 값을 넣어주세요.");

    }

    @Test
    @DisplayName("[rename] 원본 객체는 변하지 않는다.")
    void original_instance_not_changed() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        String newName = "new";

        // when
        Category afterRename = original.rename(newName, ownerId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(original.getId())
                        .isEqualTo(categoryId),
                () -> assertThat(original.getOwnerId())
                        .isEqualTo(ownerId),
                () -> assertThat(original.getName())
                        .isEqualTo(originName),
                () -> assertThat(original.getSortKey())
                        .isEqualTo(sortKey),
                () -> assertThat(original.getCreatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getUpdatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getCreatedUser())
                        .isEqualTo(ownerId),
                () -> assertThat(original.getUpdatedUser())
                        .isEqualTo(ownerId)
        );

    }


    @Test
    @DisplayName("[rename] 새객체는 name 외의 원본의 값들을 그대로 가져간다.")
    void new_instance_has_originals_value_without_name() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        String newName = "new";

        // when
        Category afterRename = original.rename(newName, ownerId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(afterRename.getId())
                        .isEqualTo(original.getId()),
                () -> assertThat(afterRename.getOwnerId())
                        .isEqualTo(original.getOwnerId()),
                () -> assertThat(afterRename.getSortKey())
                        .isEqualTo(original.getSortKey()),
                () -> assertThat(afterRename.getCreatedAt())
                        .isEqualTo(original.getCreatedAt()),
                () -> assertThat(afterRename.getCreatedUser())
                        .isEqualTo(original.getCreatedUser()),
                () -> assertThat(afterRename.getId())
                        .isEqualTo(categoryId),
                () -> assertThat(afterRename.getName())
                        .isEqualTo(newName)
        );

    }


    @Test
    @DisplayName("[rename] newName 이 null 이면 IllegalArgumentException 이 터진다.")
    void when_newName_isNull_should_throw_IllegalArgumentException() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        String newName = null;

        // when
        assertThatThrownBy(() -> original.rename(newName, ownerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");

    }


    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\n", "\t"})
    @DisplayName("[rename] newName 이 blank 이면 IllegalArgumentException 이 터진다.")
    void when_newName_isBlank_should_throw_IllegalArgumentException(String input) {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);

        // when
        assertThatThrownBy(() -> original.rename(input, ownerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");

    }

    @Test
    @DisplayName("[rename] requsterId 가 null 이면 IllegalArgumentException 이 터진다.")
    void when_requesterId_isNull_should_throw_IllegalArgumentException() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        Long requesterId = null;
        String newName = "new";

        // when
        assertThatThrownBy(() -> original.rename(newName, requesterId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수정자 id는 필수입니다.");

    }

    @Test
    @DisplayName("[rename] 새객체에 updatedAt 항목이 업데이트 된다.")
    void new_instance_has_new_updatedAt_and_updatedUser() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        String newName = "new";

        // when
        Category rename = original.rename(newName, ownerId);

        assertThat(rename.getUpdatedAt())
                .isAfter(original.getUpdatedAt());
    }

    @Test
    @DisplayName("[rename] owner 와 requester 가 다르면 권한없음 예외가 터진다.")
    void when_call_rename_ownerId_isNotEqualTo_requesterId_should_throw_accessException() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        String newName = "new";
        Long request = 3L;

        // when, then
        assertThatThrownBy(() -> original.rename(newName, request))
                .hasCauseInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("권한이 없습니다.");

    }

    @Test
    @DisplayName("[reorder] 원본 객체는 변하지 않는다.")
    void when_reordered_original_instance_not_changed() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        int newSortKey = 200;

        // when
        Category reorder = original.reorder(newSortKey, ownerId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(original.getId())
                        .isEqualTo(categoryId),
                () -> assertThat(original.getOwnerId())
                        .isEqualTo(ownerId),
                () -> assertThat(original.getName())
                        .isEqualTo(originName),
                () -> assertThat(original.getSortKey())
                        .isEqualTo(sortKey),
                () -> assertThat(original.getCreatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getUpdatedAt())
                        .isEqualTo(now),
                () -> assertThat(original.getCreatedUser())
                        .isEqualTo(ownerId),
                () -> assertThat(original.getUpdatedUser())
                        .isEqualTo(ownerId)
        );

    }


    @Test
    @DisplayName("[reorder] 새객체는 sortKey 외의 원본의 값들을 그대로 가져간다.")
    void new_instance_has_originals_value_without_sortKey() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        int newSortKey = 200;

        // when
        Category reorder = original.reorder(newSortKey, ownerId);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(reorder.getId())
                        .isEqualTo(original.getId()),
                () -> assertThat(reorder.getOwnerId())
                        .isEqualTo(original.getOwnerId()),
                () -> assertThat(reorder.getName())
                        .isEqualTo(original.getName()),
                () -> assertThat(reorder.getCreatedAt())
                        .isEqualTo(original.getCreatedAt()),
                () -> assertThat(reorder.getCreatedUser())
                        .isEqualTo(original.getCreatedUser()),
                () -> assertThat(reorder.getSortKey())
                        .isEqualTo(newSortKey)
        );

    }

    @Test
    @DisplayName("[reorder] requsterId 가 null 이면 IllegalArgumentException 이 터진다.")
    void when_call_reorder_requesterId_isNull_should_throw_IllegalArgumentException() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        Long requesterId = null;
        int newSortKey = 200;

        // when
        assertThatThrownBy(() -> original.reorder(newSortKey, requesterId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수정자 id는 필수입니다.");

    }

    @Test
    @DisplayName("[reorder] 새객체에 updatedAt항목이 업데이트 된다.")
    void when_reordered_new_instance_has_new_updatedAt_and_updatedUser() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        int newSortKey = 200;

        // when
        Category reorder = original.reorder(newSortKey, ownerId);

        assertThat(reorder.getUpdatedAt())
                .isAfter(original.getUpdatedAt());
    }


    @Test
    @DisplayName("[reorder] owner 와 requester 가 다르면 권한없음 예외가 터진다.")
    void when_call_reorder_ownerId_isNotEqualTo_requesterId_should_throw_accessException() {
        // given
        Long categoryId = 1L;
        Long ownerId = 2L;
        String originName = "origin";
        int sortKey = 100;
        Instant now = Instant.now();
        Category original = new Category(categoryId, ownerId, originName, sortKey, now, now, ownerId, ownerId);
        int newSortKey = 200;
        Long request = 3L;

        // when, then
        assertThatThrownBy(() -> original.reorder(newSortKey, request))
                .hasCauseInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("권한이 없습니다.");

    }


}