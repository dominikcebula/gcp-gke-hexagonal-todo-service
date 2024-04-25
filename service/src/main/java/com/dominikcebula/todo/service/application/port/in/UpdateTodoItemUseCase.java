package com.dominikcebula.todo.service.application.port.in;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

public interface UpdateTodoItemUseCase {
    void updateTodoItem(TodoItem todoItem);
}
