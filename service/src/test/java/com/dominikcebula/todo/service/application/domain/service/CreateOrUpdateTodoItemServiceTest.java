package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.InMemoryTodoItemsRepository;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemResultType.ITEM_CREATED;
import static com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemResultType.ITEM_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CreateOrUpdateTodoItemServiceTest {
    @Autowired
    private CreateOrUpdateTodoItemUseCase createOrUpdateTodoItemUseCase;
    @Autowired
    private InMemoryTodoItemsRepository inMemoryTodoItemsRepository;

    @Test
    void shouldCreateItemWhenItemDidNotExist() {
        // given
        var todoItem1 = new TodoItem("TodoItem1", true);
        var todoItem2 = new TodoItem("TodoItem2", false);
        var todoItem3 = new TodoItem("TodoItem3", true);
        savedTodoItems(todoItem1, todoItem2, todoItem3);

        var itemToCreate = new TodoItem("TodoItem4", true);

        // when
        var command = new CreateOrUpdateTodoItemCommand(itemToCreate.getUUID(), itemToCreate.getName(), itemToCreate.getCompleted());
        var useCaseResult = createOrUpdateTodoItemUseCase.createOrUpdateTodoItem(command);

        // then
        assertThat(useCaseResult.resultType()).isEqualTo(ITEM_CREATED);
        assertThat(useCaseResult.todoItem()).isEqualTo(itemToCreate);
        assertThatItemWasSaved(useCaseResult.todoItem());
    }

    @Test
    void shouldUpdateItemWhenItemDidExist() {
        // given
        var todoItem1 = new TodoItem("TodoItem1", true);
        var todoItem2 = new TodoItem("TodoItem2", false);
        var todoItem3 = new TodoItem("TodoItem3", true);
        savedTodoItems(todoItem1, todoItem2, todoItem3);

        var itemToUpdate = new TodoItem(todoItem2.getUUID(), "UpdatedTodoItem", true);

        // when
        var command = new CreateOrUpdateTodoItemCommand(itemToUpdate.getUUID(), itemToUpdate.getName(), itemToUpdate.getCompleted());
        var useCaseResult = createOrUpdateTodoItemUseCase.createOrUpdateTodoItem(command);

        // then
        assertThat(useCaseResult.resultType()).isEqualTo(ITEM_UPDATED);
        assertThatItemWasUpdated(useCaseResult, itemToUpdate, todoItem2);
        assertThatItemWasSaved(useCaseResult.todoItem());
    }

    private void assertThatItemWasUpdated(CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemUseCaseResult useCaseResult, TodoItem itemToUpdate, TodoItem todoItem2) {
        assertThat(useCaseResult.todoItem()).isEqualTo(itemToUpdate);
        assertThat(useCaseResult.todoItem().getUUID()).isEqualTo(todoItem2.getUUID());
        assertThat(useCaseResult.todoItem().getName()).isNotEqualTo(todoItem2.getName());
        assertThat(useCaseResult.todoItem().getCompleted()).isNotEqualTo(todoItem2.getCompleted());
    }

    private void savedTodoItems(TodoItem... todoItems) {
        for (TodoItem todoItem : todoItems) {
            inMemoryTodoItemsRepository.save(todoItem);
        }
    }

    private void assertThatItemWasSaved(TodoItem itemToCreateOrUpdate) {
        inMemoryTodoItemsRepository.exists(itemToCreateOrUpdate.getUUID());
        assertThat(inMemoryTodoItemsRepository.getById(itemToCreateOrUpdate.getUUID()))
                .isEqualTo(itemToCreateOrUpdate);
    }
}