package com.dominikcebula.todo.service.application.port.in;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

import java.util.UUID;

public interface GetTodoItemByIdUseCase {
    TodoItem getTodoItemById(UUID id);
}
