package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.api.TodosApi;
import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.in.*;
import com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult;
import com.dominikcebula.todo.service.model.TodoItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.dominikcebula.todo.service.application.port.in.DeleteTodoItemUseCase.DeleteTodoItemUseCaseResult.ITEM_DELETED;

@RequiredArgsConstructor
@RestController
public class TodoItemsController implements TodosApi {
    private final CreateTodoItemUseCase createTodoItemUseCase;
    private final GetAllTodoItemsUseCase getAllTodoItemsUseCase;
    private final GetTodoItemByIdUseCase getTodoItemByIdUseCase;
    private final DeleteTodoItemUseCase deleteTodoItemUseCase;
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

    @Override
    public ResponseEntity<TodoItemDto> getTodoItemById(UUID id) {
        TodoItem todoItem = getTodoItemByIdUseCase.getTodoItemById(id);

        if (todoItem != null) {
            TodoItemDto todoItemDto = todoItemsMapper.mapTodoItemModelToDto(todoItem);

            return ResponseEntity.ok(todoItemDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteTodoItemById(UUID todoId) {
        DeleteTodoItemUseCaseResult deletionResult = deleteTodoItemUseCase.deleteTodoItem(todoId);

        if (deletionResult == ITEM_DELETED) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
