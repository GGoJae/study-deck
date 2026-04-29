package org.example.core.category.factory;

import org.assertj.core.api.Assertions;
import org.example.core.category.domain.Category;
import org.example.core.category.factory.sortcalcultor.CategorySortCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryFactoryV1Test {

    @Mock
    private CategorySortCalculator sortCalculator;
    @InjectMocks
    private CategoryFactoryV1 categoryFactory;

    @Test
    @DisplayName("[create] id 는 null 인 채로 생성된다.")
    void when_create_categoryId_isNull() {
        // given
        Long requesterId = 1L;
        String categoryName = "name";
        int sortKey = 1000;
        when(sortCalculator.newSortKey(requesterId, sortKey)).thenReturn(sortKey);

        // when
        Category category = categoryFactory.create(requesterId, categoryName, sortKey);

        // then
        Assertions.assertThat(category.getId())
                .isNull();
    }

    @Test
    @DisplayName("[create] 생성시에 createdAt, updatedAt 은 같다.")
    void when_create_createdAt_the_same_as_updatedAt() {
        // given
        Long requesterId = 1L;
        String categoryName = "name";
        int sortKey = 1000;
        when(sortCalculator.newSortKey(requesterId, sortKey)).thenReturn(sortKey);

        // when
        Category category = categoryFactory.create(requesterId, categoryName, sortKey);

        // then
        Assertions.assertThat(category.getCreatedAt())
                .isEqualTo(category.getUpdatedAt());
    }

    @Test
    @DisplayName("[create] 생성시에 ownerId, createdUser, updatedUser 는 요청자의 id 다.")
    void when_create_all_of_ownerId_createdUser_updatedUser_is_requesterId() {
        // given
        Long requesterId = 1L;
        String categoryName = "name";
        int sortKey = 1000;
        when(sortCalculator.newSortKey(requesterId, sortKey)).thenReturn(sortKey);

        // when
        Category category = categoryFactory.create(requesterId, categoryName, sortKey);

        // then
        Assertions.assertThat(requesterId)
                .isEqualTo(category.getOwnerId())
                .isEqualTo(category.getCreatedUser())
                .isEqualTo(category.getUpdatedUser());
    }

    @Test
    @DisplayName("[create] 도중에 Calculator 에게 올바른 파라미터를 전달하며 호출한다.")
    void when_create_should_call_calculator_with_correct_params() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = 500;

        // when
        categoryFactory.create(ownerId, "test", hopeSortKey);

        // then
        verify(sortCalculator, times(1)).newSortKey(ownerId, hopeSortKey);
    }

    @Test
    @DisplayName("[create] 생성된 시간은 현재시간 근처여야 한다.")
    void when_create_time_should_be_now() {
        // given
        Long requesterId = 1L;
        String categoryName = "name";
        int sortKey = 1000;
        when(sortCalculator.newSortKey(requesterId, sortKey)).thenReturn(sortKey);
        Instant before = Instant.now();

        // when
        Category category = categoryFactory.create(requesterId, categoryName, sortKey);
        Instant after = Instant.now();

        // then
        Assertions.assertThat(category.getCreatedAt())
                .isBetween(before, after);
    }

    @Test
    @DisplayName("[create] name 에 null 이 들어오면 category 생성자에서 예외가 터져서 생성이 실패한다.")
    void when_name_isBlank_fail_create() {
        // given
        Long requesterId = 1L;
        String categoryName = null;
        int sortKey = 1000;
        when(sortCalculator.newSortKey(1L, sortKey)).thenReturn(sortKey);

        // when
        Assertions.assertThatThrownBy(() -> categoryFactory.create(requesterId, categoryName, sortKey))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("[create] sortKeyCalculator 가 주는 sortKey 를 반영한다.")
    void when_create_sortKey_is_sortKeyCalculators_returned_value() {
        // given
        int hopeKey = 500;
        int expectedKey = 1000;
        when(sortCalculator.newSortKey(anyLong(), anyInt())).thenReturn(expectedKey);

        // when
        Category category = categoryFactory.create(1L, "name", hopeKey);

        // then
        Assertions.assertThat(category.getSortKey())
                .isEqualTo(expectedKey);
    }

}