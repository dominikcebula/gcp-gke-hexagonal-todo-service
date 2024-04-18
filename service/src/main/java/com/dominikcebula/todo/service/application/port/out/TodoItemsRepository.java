package com.dominikcebula.todo.service.application.port.out;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

import java.util.List;

public interface TodoItemsRepository {
    void save(TodoItem todoItem);

    List<TodoItem> getAll();
}
