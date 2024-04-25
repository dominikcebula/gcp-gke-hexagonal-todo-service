package com.dominikcebula.todo.service.application.port.in;

import java.util.UUID;

public record CreateOrUpdateTodoItemCommand(UUID id, String name, boolean completed) {
}
