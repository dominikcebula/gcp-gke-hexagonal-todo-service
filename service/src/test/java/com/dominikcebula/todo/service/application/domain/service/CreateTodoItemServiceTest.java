package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.InMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase.CreateTodoItemCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class CreateTodoItemServiceTest {
    @Autowired
    private CreateTodoItemUseCase createTodoItemUseCase;
    @Autowired
    private InMemoryTodoItemsRepository inMemoryTodoItemsRepository;

    @Test
    void shouldCreateTodoItemWithCompletedState() {
        // given
        var todoItemName = "TestTodoItem01";
        var todoItemCompleted = true;
        var command = new CreateTodoItemCommand(todoItemName, todoItemCompleted);

        // when
        TodoItem todoItem = createTodoItemUseCase.createTodoItem(command);

        // then
        assertTodoItemCreated(todoItem, todoItemName, todoItemCompleted);
    }

    @Test
    void shouldCreateTodoItemWithInCompletedState() {
        // given
        var todoItemName = "TestTodoItem02";
        var todoItemCompleted = false;
        var command = new CreateTodoItemCommand(todoItemName, todoItemCompleted);

        // when
        TodoItem todoItem = createTodoItemUseCase.createTodoItem(command);

        // then
        assertTodoItemCreated(todoItem, todoItemName, todoItemCompleted);
    }

    @Test
    void shouldAllowTodoItemsWithTheSameNames() {
        // given
        var todoItemName = "TestTodoItem03";
        var todoItemCompleted = false;
        var command1 = new CreateTodoItemCommand(todoItemName, todoItemCompleted);
        var command2 = new CreateTodoItemCommand(todoItemName, todoItemCompleted);

        // when
        TodoItem todoItem1 = createTodoItemUseCase.createTodoItem(command1);
        TodoItem todoItem2 = createTodoItemUseCase.createTodoItem(command2);

        // then
        assertTodoItemCreated(todoItem1, todoItemName, todoItemCompleted);
        assertTodoItemCreated(todoItem2, todoItemName, todoItemCompleted);
        assertTodoItemNameAndCompletedEqual(todoItem1, todoItem2);
    }

    private void assertTodoItemCreated(TodoItem todoItem, String todoItemName, boolean todoItemCompleted) {
        assertThat(todoItem.getId()).isNotBlank();
        assertThat(todoItem.getName()).isEqualTo(todoItemName);
        assertThat(todoItem.getCompleted()).isEqualTo(todoItemCompleted);
        assertThat(inMemoryTodoItemsRepository.exists(todoItem.getUUID()))
                .isTrue();
        assertThat(inMemoryTodoItemsRepository.getById(todoItem.getUUID()))
                .isEqualTo(todoItem);
    }

    private void assertTodoItemNameAndCompletedEqual(TodoItem todoItem1, TodoItem todoItem2) {
        assertThat(todoItem1.getName()).isEqualTo(todoItem2.getName());
        assertThat(todoItem1.getCompleted()).isEqualTo(todoItem2.getCompleted());
    }
}