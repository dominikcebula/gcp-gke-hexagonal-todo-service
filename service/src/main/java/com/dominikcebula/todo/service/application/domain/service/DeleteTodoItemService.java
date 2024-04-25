package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult.ITEM_DELETED;
import static com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult.ITEM_DID_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class DeleteTodoItemService implements DeleteTodoItemUseCase {
    private final TodoItemsRepository repository;

    @Override
    public DeleteTodoItemUseCaseResult deleteTodoItem(UUID id) {
        if (repository.exists(id)) {
            repository.deleteById(id);
            return ITEM_DELETED;
        } else {
            return ITEM_DID_NOT_EXIST;
        }
    }
}
