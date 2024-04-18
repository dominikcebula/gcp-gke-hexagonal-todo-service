package com.dominikcebula.todo.service.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class TodoItem {
    private final String id;
    private final String name;
    private final Boolean completed;

    @SuppressWarnings("unused")
    TodoItem() {
        id = null;
        name = null;
        completed = null;
    }

    public TodoItem(String name, boolean completed) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.completed = completed;
    }
}
