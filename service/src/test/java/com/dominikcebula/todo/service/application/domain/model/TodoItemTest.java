package com.dominikcebula.todo.service.application.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TodoItemTest {
    @Test
    void shouldCreateTodoItemWithGeneratedId() {
        // given
        final var todoItemName = "TodoItem01";
        final var todoItemCompletedStatus = true;

        // when
        var todoItem = new TodoItem(todoItemName, todoItemCompletedStatus);

        // then
        assertTodoItemId(todoItem);
        assertThat(todoItem.getName())
                .isEqualTo(todoItemName);
        assertThat(todoItem.getCompleted())
                .isEqualTo(todoItemCompletedStatus);
    }

    @Test
    void shouldCreateTodoItemWithGivenId() {
        // given
        final var todoItemId = UUID.randomUUID();
        final var todoItemName = "TodoItem01";
        final var todoItemCompletedStatus = true;

        // when
        var todoItem = new TodoItem(todoItemId, todoItemName, todoItemCompletedStatus);

        // then
        assertThat(todoItem.getUUID())
                .isEqualTo(todoItemId);
        assertThat(todoItem.getId())
                .isEqualTo(todoItemId.toString());
        assertThat(todoItem.getName())
                .isEqualTo(todoItemName);
        assertThat(todoItem.getCompleted())
                .isEqualTo(todoItemCompletedStatus);
    }

    @Test
    void shouldHaveDefaultConstructorToSupportDocumentDatabase() {
        // when
        var todoItem = new TodoItem();

        // then
        assertThat(todoItem.getId()).isNull();
        assertThat(todoItem.getName()).isNull();
        assertThat(todoItem.getCompleted()).isNull();
    }

    private void assertTodoItemId(TodoItem todoItem) {
        assertThat(todoItem.getId())
                .isNotBlank();
        assertThat(todoItem.getUUID())
                .isNotNull();
        assertThat(todoItem.getId())
                .isEqualTo(todoItem.getUUID().toString());
    }
}