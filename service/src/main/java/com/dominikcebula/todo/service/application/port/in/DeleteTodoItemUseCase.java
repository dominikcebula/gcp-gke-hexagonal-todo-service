package com.dominikcebula.todo.service.application.port.in;

import java.util.UUID;

public interface DeleteTodoItemUseCase {
    DeleteTodoItemUseCaseResult deleteTodoItem(UUID todoId);

    enum DeleteTodoItemUseCaseResult {
        ITEM_DELETED,
        ITEM_DID_NOT_EXIST
    }
}
