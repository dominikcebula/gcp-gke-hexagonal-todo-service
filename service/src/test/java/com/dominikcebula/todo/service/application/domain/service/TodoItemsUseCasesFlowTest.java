package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.WithInMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.*;
import com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemCommand;
import com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemUseCaseResult;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase.CreateTodoItemCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemResultType.ITEM_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithInMemoryTodoItemsRepository
@DirtiesContext
public class TodoItemsUseCasesFlowTest {
    @Autowired
    private CreateTodoItemUseCase createTodoItemUseCase;
    @Autowired
    private CreateOrUpdateTodoItemUseCase createOrUpdateTodoItemUseCase;
    @Autowired
    private GetAllTodoItemsUseCase getAllTodoItemsUseCase;
    @Autowired
    private GetTodoItemByIdUseCase getTodoItemByIdUseCase;
    @Autowired
    private DeleteTodoItemUseCase deleteTodoItemUseCase;

    @Test
    void shouldCreateUpdateListAndDeleteTodoItems() {
        // given create data
        var createCommand1 = new CreateTodoItemCommand("TodoItem01", true);
        var createCommand2 = new CreateTodoItemCommand("TodoItem02", false);
        var createCommand3 = new CreateTodoItemCommand("TodoItem03", true);
        var createCommand4 = new CreateTodoItemCommand("TodoItem04", false);

        // when creating todo items
        var todoItem1 = createTodoItemUseCase.createTodoItem(createCommand1);
        var todoItem2 = createTodoItemUseCase.createTodoItem(createCommand2);
        var todoItem3 = createTodoItemUseCase.createTodoItem(createCommand3);
        var todoItem4 = createTodoItemUseCase.createTodoItem(createCommand4);

        // then assert items created
        assertCreated(todoItem1, todoItem2, todoItem3, todoItem4);

        // and given update data
        var updateCommand1 = new CreateOrUpdateTodoItemCommand(todoItem2.getUUID(), "UpdatedTodoItem02", true);
        var updateCommand2 = new CreateOrUpdateTodoItemCommand(todoItem4.getUUID(), "UpdatedTodoItem04", false);

        // and when updating todo items
        var updateResult1 = createOrUpdateTodoItemUseCase.createOrUpdateTodoItem(updateCommand1);
        var updateResult2 = createOrUpdateTodoItemUseCase.createOrUpdateTodoItem(updateCommand2);

        // then assert updates results
        assertUpdateResult(updateResult1, todoItem2, "UpdatedTodoItem02", true);
        assertUpdateResult(updateResult2, todoItem4, "UpdatedTodoItem04", false);

        // and then assert items updated
        assertTodoItemData(todoItem2.getUUID(), "UpdatedTodoItem02", true);
        assertTodoItemData(todoItem4.getUUID(), "UpdatedTodoItem04", false);

        // and after first item was deleted
        deleteTodoItemUseCase.deleteTodoItem(todoItem1.getUUID());

        // then assert all todo items state
        assertTodoItemDataDeleted(todoItem1.getUUID());
        assertTodoItemData(todoItem2.getUUID(), "UpdatedTodoItem02", true);
        assertTodoItemData(todoItem3.getUUID(), "TodoItem03", true);
        assertTodoItemData(todoItem4.getUUID(), "UpdatedTodoItem04", false);

        // and after all items are deleted
        deleteTodoItemUseCase.deleteTodoItem(todoItem2.getUUID());
        deleteTodoItemUseCase.deleteTodoItem(todoItem3.getUUID());
        deleteTodoItemUseCase.deleteTodoItem(todoItem4.getUUID());

        // then assert all items deleted
        assertTodoItemDataDeleted(todoItem1.getUUID());
        assertTodoItemDataDeleted(todoItem2.getUUID());
        assertTodoItemDataDeleted(todoItem3.getUUID());
        assertTodoItemDataDeleted(todoItem4.getUUID());
        assertThat(getAllTodoItemsUseCase.getAllTodoItems()).isEmpty();
    }

    private void assertCreated(TodoItem... todoItems) {
        for (TodoItem todoItem : todoItems) {
            assertThat(todoItem).isNotNull();
            assertThat(todoItem.getId()).isNotBlank();
            assertThat(todoItem.getUUID()).isNotNull();
            assertThat(todoItem.getName()).isNotBlank();
            assertThat(todoItem.getCompleted()).isNotNull();
            assertThat(getTodoItemByIdUseCase.getTodoItemById(todoItem.getUUID())).isNotNull();
            assertThat(getTodoItemByIdUseCase.getTodoItemById(todoItem.getUUID())).isEqualTo(todoItem);
            assertThat(getAllTodoItemsUseCase.getAllTodoItems())
                    .contains(todoItem);
        }
    }

    private void assertUpdateResult(CreateOrUpdateTodoItemUseCaseResult updateResult, TodoItem todoItem, String name, boolean completed) {
        assertThat(updateResult.resultType()).isEqualTo(ITEM_UPDATED);
        assertThat(updateResult.todoItem()).isEqualTo(new TodoItem(todoItem.getUUID(), name, completed));
    }

    private void assertTodoItemData(UUID id, String name, Boolean completed) {
        TodoItem todoItem = getTodoItemByIdUseCase.getTodoItemById(id);

        assertThat(todoItem.getUUID()).isEqualTo(id);
        assertThat(todoItem.getName()).isEqualTo(name);
        assertThat(todoItem.getCompleted()).isEqualTo(completed);

        assertThat(getAllTodoItemsUseCase.getAllTodoItems())
                .contains(new TodoItem(id, name, completed));
    }

    private void assertTodoItemDataDeleted(UUID id) {
        assertThat(getTodoItemByIdUseCase.getTodoItemById(id))
                .isNull();
    }
}
