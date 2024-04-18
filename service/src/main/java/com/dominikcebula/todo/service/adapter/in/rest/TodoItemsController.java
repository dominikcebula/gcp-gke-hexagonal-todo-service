package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.api.TodosApi;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemCommand;
import com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase;
import com.dominikcebula.todo.service.application.port.in.GetAllTodoItemsUseCase;
import com.dominikcebula.todo.service.model.TodoItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoItemsController implements TodosApi {
    private final CreateTodoItemUseCase createTodoItemUseCase;
    private final GetAllTodoItemsUseCase getAllTodoItemsUseCase;
    private final TodoItemsMapper todoItemsMapper;

    @Override
    public ResponseEntity<TodoItemDto> createTodoItem(TodoItemDto todoItemToCreate) {
        CreateTodoItemCommand createTodoItemCommand = todoItemsMapper.mapTodoItemDtoToCreateCommand(todoItemToCreate);

        TodoItem createdTodoItem = createTodoItemUseCase.createTodoItem(createTodoItemCommand);

        TodoItemDto createdTodoItemDto = todoItemsMapper.mapTodoItemModelToDto(createdTodoItem);

        return ResponseEntity.ok(createdTodoItemDto);
    }

    @Override
    public ResponseEntity<List<TodoItemDto>> getTodoItems() {
        List<TodoItem> todoItems = getAllTodoItemsUseCase.getAllTodoItems();

        List<TodoItemDto> todoItemDtos = todoItemsMapper.mapTodoItemsModelToDtos(todoItems);

        return ResponseEntity.ok().body(todoItemDtos);
    }
}
