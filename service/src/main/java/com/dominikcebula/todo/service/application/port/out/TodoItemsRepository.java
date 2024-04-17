package com.dominikcebula.todo.service.application.port.out;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

public interface TodoItemsRepository {
    void save(TodoItem todoItem);
}
