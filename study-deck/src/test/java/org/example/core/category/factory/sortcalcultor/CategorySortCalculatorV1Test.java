package org.example.core.category.factory.sortcalcultor;

import org.example.core.category.port.out.CategoryStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategorySortCalculatorV1Test {

    @Mock
    private CategoryStore categoryStore;
    @InjectMocks
    private CategorySortCalculatorV1 sortCalculator;
    private static final int AMOUNT = 1000;

    @Test
    @DisplayName("[newSortKey] ownerId 에 null 이 들어가면 NullPointerException이 터진다.")
    void when_ownerId_isNull_should_throw_NullPointerException() {
        // given
        Long ownerId = null;
        Integer hopeSortKey = 1000;

        // when, then
        assertThatThrownBy(() -> sortCalculator.newSortKey(ownerId, hopeSortKey))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    @DisplayName("[newSortKey] store 에 다른 카테고리가 없고, hope가 null 이면 기본 AMOUNT 인 1000이 된다.")
    void when_sortKeys_empty_and_hopeSortKey_isNull_default_sortKey_is_1000() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = null;
        List<Integer> sortKeys = List.of();
        when(categoryStore.findAllCategorySortKey(any())).thenReturn(sortKeys);

        // when
        int newSortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);

        // then
        assertThat(newSortKey)
                .isEqualTo(AMOUNT);
    }

    @Test
    @DisplayName("[newSortKey] sortKeys 의 최대값과 hopeSortKey 가 같으면 hopeSortKey + amount 된 값이 나온다.")
    void when_sortKeys_contains_hopeKey_finalKey_is_hopeSortKey_plus_amount() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = 1000;
        List<Integer> sortKeys = List.of(1000);
        when(categoryStore.findAllCategorySortKey(any())).thenReturn(sortKeys);

        // when
        int newSortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);

        // then
        assertThat(newSortKey)
                .isEqualTo(AMOUNT + AMOUNT);
    }

    @Test
    @DisplayName("[newSortKey] hopeKey 키가 sortKey에 포함되어있으면 그 다음 값과 hopeKey 의 중간 값으로 결정된다.")
    void when_sortKeys_contains_finalKey_finalKey_decide_intermediate_value() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = 1000;
        int sortKey1 = 1000;
        int sortKey2 = 2000;
        List<Integer> sortKeys = List.of(sortKey1, sortKey2);
        when(categoryStore.findAllCategorySortKey(any())).thenReturn(sortKeys);

        // when
        int newSortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);

        // then
        assertThat(newSortKey)
                .isEqualTo((hopeSortKey + sortKey2) / 2);
    }


    @Test
    @DisplayName("[newSortKey] hopeKey 가 null 인데 sortKey가 존재하면 finalKey 는 max값 + AMOUNT 값이 된다.")
    void when_hopeKey_isNull_and_sortKey_notEmpty_finalKey_decide_max_plus_amount() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = null;
        int sortKey1 = 1000;
        int sortKey2 = 1001;
        int sortKey3 = 2000;
        int sortKey4 = 3000;
        List<Integer> sortKeys = List.of(sortKey1, sortKey2, sortKey3, sortKey4);
        when(categoryStore.findAllCategorySortKey(any())).thenReturn(sortKeys);

        // when
        int newSortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);

        // then
        assertThat(newSortKey)
                .isEqualTo(sortKeys.stream().max(Integer::compareTo).orElseThrow() + AMOUNT);

    }

    @Test
    @DisplayName("[newSortKey] 두 값 사이의 중간값이 정수형 계산으로 인해 기존 값과 같아지면, 중복된 sortKey 가 생성된다.")
    void when_intermediate_value_is_same_as_hope_due_to_integer_division() {
        // given
        Long ownerId = 1L;
        Integer hopeSortKey = 1000;
        List<Integer> sortKeys = List.of(1000, 1001, 2000);
        when(categoryStore.findAllCategorySortKey(any())).thenReturn(sortKeys);

        // when
        int newSortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);

        // then
        assertThat(newSortKey)
                .isEqualTo(hopeSortKey);
    }

}