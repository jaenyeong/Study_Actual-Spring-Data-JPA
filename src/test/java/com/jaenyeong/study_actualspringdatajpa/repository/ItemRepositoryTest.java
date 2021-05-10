package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("식별자 전략 테스트")
    void entityPrimaryKeyStrategy() throws Exception {
        // Arrange
        final Item item = new Item(1L);

        // Act
        itemRepository.save(item);
        final Item findItem = itemRepository.findById(1L)
            .orElseGet(() -> new Item(2L));

        // Assert
        assertThat(findItem.getId()).isEqualTo(1L);
    }
}
