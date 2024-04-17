package com.dominikcebula.todo.service.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class TodoItem {
    private final UUID id;
    private final String name;
    private final boolean completed;

    public TodoItem(String name, boolean completed) {
        id = UUID.randomUUID();
        this.name = name;
        this.completed = completed;
    }
}
