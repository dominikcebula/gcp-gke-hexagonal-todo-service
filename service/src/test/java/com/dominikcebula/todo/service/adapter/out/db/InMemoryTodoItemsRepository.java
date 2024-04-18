package com.dominikcebula.todo.service.adapter.out.db;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryTodoItemsRepository implements TodoItemsRepository {
    @Override
    public void save(TodoItem todoItem) {

    }

    @Override
    public List<TodoItem> getAll() {
        return List.of();
    }
}
