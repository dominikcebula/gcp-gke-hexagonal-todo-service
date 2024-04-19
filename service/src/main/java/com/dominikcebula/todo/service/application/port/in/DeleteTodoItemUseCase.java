package com.dominikcebula.todo.service.application.port.in;

import java.util.UUID;

public interface DeleteTodoItemUseCase {
    void deleteTodoItem(UUID todoId);
}
