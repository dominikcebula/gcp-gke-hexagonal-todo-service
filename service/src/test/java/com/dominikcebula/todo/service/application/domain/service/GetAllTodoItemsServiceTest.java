package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.InMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.GetAllTodoItemsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GetAllTodoItemsServiceTest {
    @Autowired
    private GetAllTodoItemsUseCase getAllTodoItemsUseCase;
    @Autowired
    private InMemoryTodoItemsRepository inMemoryTodoItemsRepository;

    @Test
    @DirtiesContext
    void shouldGetAllTodoItems() {
        // given
        var todoItem1 = new TodoItem("TodoItem1", true);
        var todoItem2 = new TodoItem("TodoItem2", false);
        var todoItem3 = new TodoItem("TodoItem3", true);
        savedTodoItems(todoItem1, todoItem2, todoItem3);

        // when
        List<TodoItem> savedTodoItems = getAllTodoItemsUseCase.getAllTodoItems();

        // then
        assertThat(savedTodoItems)
                .containsOnly(
                        todoItem1,
                        todoItem2,
                        todoItem3
                );
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsSaved() {
        // when
        List<TodoItem> savedTodoItems = getAllTodoItemsUseCase.getAllTodoItems();

        // then
        assertThat(savedTodoItems)
                .isEmpty();
    }

    private void savedTodoItems(TodoItem... todoItems) {
        for (TodoItem todoItem : todoItems) {
            inMemoryTodoItemsRepository.save(todoItem);
        }
    }
}