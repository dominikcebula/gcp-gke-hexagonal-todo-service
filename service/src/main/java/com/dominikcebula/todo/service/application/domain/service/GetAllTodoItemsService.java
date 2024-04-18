package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.GetAllTodoItemsUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllTodoItemsService implements GetAllTodoItemsUseCase {
    private final TodoItemsRepository repository;

    @Override
    public List<TodoItem> getAllTodoItems() {
        return repository.getAll();
    }
}
