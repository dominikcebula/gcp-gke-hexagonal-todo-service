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
    private final String description;
    private final boolean completed;

    public TodoItem(String description) {
        id = UUID.randomUUID().toString();
        this.description = description;
        completed = false;
    }
}
