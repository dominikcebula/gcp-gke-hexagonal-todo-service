package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.GetTodoItemByIdUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetTodoItemByIdService implements GetTodoItemByIdUseCase {
    private final TodoItemsRepository repository;

    @Override
    public TodoItem getTodoItemById(UUID id) {
        return repository.getById(id);
    }
}
