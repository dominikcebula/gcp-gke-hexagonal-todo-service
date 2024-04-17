package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemCommand;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTodoItemService implements CreateTodoItemUseCase {
    private final TodoItemsRepository todoItemsRepository;

    @Override
    public TodoItem createTodoItem(CreateTodoItemCommand command) {
        TodoItem todoItem = new TodoItem(command.description());

        todoItemsRepository.save(todoItem);

        return todoItem;
    }
}
