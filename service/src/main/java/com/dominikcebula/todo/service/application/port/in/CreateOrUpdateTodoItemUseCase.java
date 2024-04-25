package com.dominikcebula.todo.service.application.port.in;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

public interface CreateOrUpdateTodoItemUseCase {
    CreateOrUpdateTodoItemUseCaseResult createOrUpdateTodoItem(CreateOrUpdateTodoItemCommand command);

    record CreateOrUpdateTodoItemUseCaseResult(TodoItem todoItem, CreateOrUpdateTodoItemResultType resultType) {
    }

    enum CreateOrUpdateTodoItemResultType {
        ITEM_CREATED,
        ITEM_UPDATED
    }
}
