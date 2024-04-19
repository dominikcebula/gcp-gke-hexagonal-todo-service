package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.client.ApiClient;
import com.dominikcebula.todo.service.client.ApiException;
import com.dominikcebula.todo.service.client.Configuration;
import com.dominikcebula.todo.service.client.api.DefaultApi;
import com.dominikcebula.todo.service.client.model.TodoItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("env-snd")
class TodosApiTest {
    @LocalServerPort
    private int serverPort;

    private DefaultApi apiClient;

    @BeforeEach
    void setUp() {
        apiClient = createApiClient();
    }

    @Test
    void shouldCreateTodoItem() throws ApiException {
        // given
        final String todoItemName = "TodoItem01";
        final boolean todoItemCompletedState = true;

        final TodoItemDto todoItemDto = new TodoItemDto()
                .name(todoItemName)
                .completed(todoItemCompletedState);

        // when
        TodoItemDto createdTodoItem = apiClient.createTodoItem(todoItemDto);
        List<TodoItemDto> allTodoItems = apiClient.getTodoItems();

        // then
        assertIdIsNotBlank(createdTodoItem);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
        assertItemSaved(allTodoItems, createdTodoItem);
    }

    @Test
    void shouldGetAllTodoItems() throws ApiException {
        // when
        List<TodoItemDto> allTodoItems = apiClient.getTodoItems();

        // then
        assertThat(allTodoItems).isNotNull();
    }

    private void assertIdIsNotBlank(TodoItemDto createdTodoItem) {
        assertThat(createdTodoItem).isNotNull();
        assertThat(createdTodoItem.getId()).isNotNull();
        assertThat(createdTodoItem.getId().toString()).isNotBlank();
    }

    private void assertNameValid(TodoItemDto createdTodoItem, String todoItemName) {
        assertThat(createdTodoItem.getName())
                .isNotBlank()
                .isEqualTo(todoItemName);
    }

    private void assertCompletedStateValid(TodoItemDto createdTodoItem, boolean todoItemCompletedState) {
        assertThat(createdTodoItem.getCompleted())
                .isNotNull()
                .isEqualTo(todoItemCompletedState);
    }

    private void assertItemSaved(List<TodoItemDto> allTodoItems, TodoItemDto createdTodoItem) {
        assertThat(allTodoItems)
                .contains(createdTodoItem);
    }

    private DefaultApi createApiClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:" + serverPort);
        return new DefaultApi(defaultClient);
    }
}