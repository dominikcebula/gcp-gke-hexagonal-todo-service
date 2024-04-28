package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.InMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.adapter.out.db.WithInMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult.ITEM_DELETED;
import static com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult.ITEM_DID_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithInMemoryTodoItemsRepository
@DirtiesContext
class DeleteTodoItemServiceTest {
    @Autowired
    private DeleteTodoItemUseCase deleteTodoItemUseCase;
    @Autowired
    private InMemoryTodoItemsRepository inMemoryTodoItemsRepository;

    @Test
    void shouldDeleteExistingTodoItem() {
        // given
        var todoItem = savedTodoItem();

        // when
        var useCaseResult = deleteTodoItemUseCase.deleteTodoItem(todoItem.getUUID());

        // then
        assertThat(useCaseResult).isEqualTo(ITEM_DELETED);
        assertItemDeleted(todoItem);
    }

    @Test
    void shouldNotDeleteNonSavedItem() {
        // given
        var nonSavedTodoItem = UUID.randomUUID();

        // when
        var useCaseResult = deleteTodoItemUseCase.deleteTodoItem(nonSavedTodoItem);

        // then
        assertThat(useCaseResult).isEqualTo(ITEM_DID_NOT_EXIST);
    }

    private TodoItem savedTodoItem() {
        var todoItem = new TodoItem("TestTodoItem", true);
        inMemoryTodoItemsRepository.save(todoItem);
        return todoItem;
    }

    private void assertItemDeleted(TodoItem todoItem) {
        assertThat(inMemoryTodoItemsRepository.exists(todoItem.getUUID()))
                .isFalse();
    }
}