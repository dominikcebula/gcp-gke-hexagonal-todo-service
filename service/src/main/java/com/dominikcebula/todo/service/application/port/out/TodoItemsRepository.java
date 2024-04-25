package com.dominikcebula.todo.service.application.port.out;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

import java.util.List;
import java.util.UUID;

public interface TodoItemsRepository {
    void save(TodoItem todoItem);

    List<TodoItem> getAll();

    TodoItem getById(UUID id);

    boolean exists(UUID id);

    void deleteById(UUID todoId);
}
