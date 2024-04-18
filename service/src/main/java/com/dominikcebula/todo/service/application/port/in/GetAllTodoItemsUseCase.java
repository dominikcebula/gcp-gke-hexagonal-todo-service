package com.dominikcebula.todo.service.application.port.in;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;

import java.util.List;

public interface GetAllTodoItemsUseCase {
    List<TodoItem> getAllTodoItems();
}
