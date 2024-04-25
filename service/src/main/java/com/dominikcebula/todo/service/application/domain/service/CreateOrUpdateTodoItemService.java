package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemResultType.ITEM_CREATED;
import static com.dominikcebula.todo.service.application.port.in.CreateOrUpdateTodoItemUseCase.CreateOrUpdateTodoItemResultType.ITEM_UPDATED;

@RequiredArgsConstructor
@Service
public class CreateOrUpdateTodoItemService implements CreateOrUpdateTodoItemUseCase {
    private final TodoItemsRepository repository;

    @Override
    public CreateOrUpdateTodoItemUseCaseResult createOrUpdateTodoItem(CreateOrUpdateTodoItemCommand command) {
        boolean didItemExist = repository.exists(command.id());

        TodoItem todoItem = new TodoItem(command.id(), command.name(), command.completed());

        repository.save(todoItem);

        if (!didItemExist) {
            return new CreateOrUpdateTodoItemUseCaseResult(todoItem, ITEM_CREATED);
        } else {
            return new CreateOrUpdateTodoItemUseCaseResult(todoItem, ITEM_UPDATED);
        }
    }
}
