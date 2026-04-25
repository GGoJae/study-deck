package org.example.core.category.service;

import org.example.core.category.domain.Category;
import org.example.core.category.factory.CategoryFactory;
import org.example.core.category.model.CreateCategory;
import org.example.core.category.port.out.CategoryStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCommandServiceV1Test {

    @Mock
    private CategoryStore store;
    @Mock
    private CategoryFactory categoryFactory;
    @InjectMocks
    private CategoryCommandServiceV1 commandService;

    @Test
    @DisplayName("[create] CategoryCreate 내부에서 store.save 와 factory.create 를 호출한다. ")
    void when_create_category_inner_part_call_stores_save_and_factorys_create() {
        // given
        long ownerId = 1L;
        when(categoryFactory.create(anyLong(), anyString(), anyInt())).thenReturn(new Category(null, 1L, "category", 100, Instant.now(), Instant.now(), 1L, 1L));
        when(store.save(any())).thenReturn(new Category(1L, 1L, "category", 100, Instant.now(), Instant.now(), 1L, 1L));
        CreateCategory model = new CreateCategory(ownerId, "category", 100);

        // when
        commandService.create(model);

        // then
        assertAll(
                () -> verify(categoryFactory).create(anyLong(), anyString(), anyInt()),
                () -> verify(store).save(any())
        );


    }

}