package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteTodoItemService implements DeleteTodoItemUseCase {
    private final TodoItemsRepository todoItemsRepository;

    @Override
    public void deleteTodoItem(UUID todoId) {
        todoItemsRepository.deleteById(todoId);
    }
}
