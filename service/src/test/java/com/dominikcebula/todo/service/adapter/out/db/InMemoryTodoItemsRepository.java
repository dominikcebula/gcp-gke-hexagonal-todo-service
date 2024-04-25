package com.dominikcebula.todo.service.adapter.out.db;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Profile("test")
public class InMemoryTodoItemsRepository implements TodoItemsRepository {
    @Override
    public void save(TodoItem todoItem) {

    }

    @Override
    public List<TodoItem> getAll() {
        return List.of();
    }

    @Override
    public TodoItem getById(UUID id) {
        return null;
    }

    @Override
    public boolean exists(UUID id) {
        return false;
    }

    @Override
    public void deleteById(UUID todoId) {

    }
}
