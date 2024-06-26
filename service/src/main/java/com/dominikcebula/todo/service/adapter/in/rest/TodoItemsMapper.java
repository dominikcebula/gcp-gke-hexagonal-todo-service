package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.model.TodoItemDto;
import org.mapstruct.Mapper;

import java.util.List;

import static com.dominikcebula.todo.service.application.port.in.CreateTodoItemUseCase.CreateTodoItemCommand;

@Mapper(componentModel = "spring")
public interface TodoItemsMapper {
    CreateTodoItemCommand mapTodoItemDtoToCreateCommand(TodoItemDto todoItem);

    TodoItemDto mapTodoItemModelToDto(TodoItem todoItem);

    List<TodoItemDto> mapTodoItemsModelToDtos(List<TodoItem> todoItem);
}
