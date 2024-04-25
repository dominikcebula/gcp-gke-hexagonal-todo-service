package com.dominikcebula.todo.service.application.port.in;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

import java.util.UUID;

public interface CreateOrUpdateTodoItemUseCase {
    CreateOrUpdateTodoItemUseCaseResult createOrUpdateTodoItem(CreateOrUpdateTodoItemCommand command);

    record CreateOrUpdateTodoItemCommand(UUID id, String name, boolean completed) {
    }

    record CreateOrUpdateTodoItemUseCaseResult(TodoItem todoItem, CreateOrUpdateTodoItemResultType resultType) {
    }

    enum CreateOrUpdateTodoItemResultType {
        ITEM_CREATED,
        ITEM_UPDATED
    }
}
