package com.dominikcebula.todo.service.adapter.out.db;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class InMemoryTodoItemsRepository implements TodoItemsRepository {
    private final Map<UUID, TodoItem> todoItems = new LinkedHashMap<>();

    @Override
    public void save(TodoItem todoItem) {
        todoItems.put(todoItem.getUUID(), todoItem);
    }

    @Override
    public List<TodoItem> getAll() {
        return new LinkedList<>(todoItems.values());
    }

    @Override
    public TodoItem getById(UUID id) {
        return todoItems.get(id);
    }

    @Override
    public boolean exists(UUID id) {
        return todoItems.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        todoItems.remove(id);
    }
}
