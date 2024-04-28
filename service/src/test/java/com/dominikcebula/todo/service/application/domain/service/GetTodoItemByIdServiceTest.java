package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.InMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.GetTodoItemByIdUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class GetTodoItemByIdServiceTest {
    @Autowired
    private GetTodoItemByIdUseCase getTodoItemByIdUseCase;
    @Autowired
    private InMemoryTodoItemsRepository inMemoryTodoItemsRepository;

    @Test
    void shouldReturnTodoItemById() {
        // given
        var todoItem1 = new TodoItem("TodoItem1", true);
        var todoItem2 = new TodoItem("TodoItem2", false);
        var todoItem3 = new TodoItem("TodoItem3", true);
        savedTodoItems(todoItem1, todoItem2, todoItem3);

        // then
        TodoItem retrievedTodoItem = getTodoItemByIdUseCase.getTodoItemById(todoItem2.getUUID());

        // then
        assertThat(retrievedTodoItem).isNotNull();
        assertThat(retrievedTodoItem).isEqualTo(todoItem2);
    }

    @Test
    void shouldReturnNullOnNonExistingItem() {
        // given
        var todoItem1 = new TodoItem("TodoItem1", true);
        var todoItem2 = new TodoItem("TodoItem2", false);
        var todoItem3 = new TodoItem("TodoItem3", true);
        savedTodoItems(todoItem1, todoItem2, todoItem3);

        // then
        TodoItem retrievedTodoItem = getTodoItemByIdUseCase.getTodoItemById(UUID.randomUUID());

        // then
        assertThat(retrievedTodoItem).isNull();
    }

    private void savedTodoItems(TodoItem... todoItems) {
        for (TodoItem todoItem : todoItems) {
            inMemoryTodoItemsRepository.save(todoItem);
        }
    }
}