package com.dominikcebula.todo.service.application.port.in;

public record CreateTodoItemCommand(String name, boolean completed) {
}
