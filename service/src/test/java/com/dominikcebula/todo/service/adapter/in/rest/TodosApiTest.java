package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.client.ApiClient;
import com.dominikcebula.todo.service.client.ApiException;
import com.dominikcebula.todo.service.client.ApiResponse;
import com.dominikcebula.todo.service.client.Configuration;
import com.dominikcebula.todo.service.client.api.DefaultApi;
import com.dominikcebula.todo.service.client.model.TodoItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("env-ci")
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

        // then
        assertIdIsNotBlank(createdTodoItem);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
        assertItemWasCreated(createdTodoItem);
    }

    @Test
    void shouldGetAllTodoItems() throws ApiException {
        // when
        List<TodoItemDto> allTodoItems = apiClient.getTodoItems();

        // then
        assertThat(allTodoItems).isNotNull();
    }

    @Test
    void shouldGetNoContentResponseWhenDeletingNonExistingItem() throws ApiException {
        // when
        ApiResponse<Void> apiResponse = apiClient.deleteTodoItemByIdWithHttpInfo(UUID.randomUUID());

        // then
        assertThat(apiResponse.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldGetNoContentResponseWhenDeletingExistingItem() throws ApiException {
        // given item definition
        final String todoItemName = "TodoItem01";
        final boolean todoItemCompletedState = true;
        final TodoItemDto todoItemDto = new TodoItemDto()
                .name(todoItemName)
                .completed(todoItemCompletedState);

        // and given created item
        TodoItemDto createdTodoItem = apiClient.createTodoItem(todoItemDto);
        assertIdIsNotBlank(createdTodoItem);
        assertItemWasCreated(createdTodoItem);

        // when item is deleted
        ApiResponse<Void> apiResponse = apiClient.deleteTodoItemByIdWithHttpInfo(createdTodoItem.getId());

        // then verify item does not exist
        assertThat(apiResponse.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
        assertItemWasRemoved(createdTodoItem);
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

    private void assertItemWasCreated(TodoItemDto createdTodoItem) throws ApiException {
        List<TodoItemDto> todoItems = apiClient.getTodoItems();

        assertItemExists(todoItems, createdTodoItem);
    }

    private void assertItemExists(List<TodoItemDto> allTodoItems, TodoItemDto createdTodoItem) {
        assertThat(allTodoItems)
                .contains(createdTodoItem);
    }

    private void assertItemWasRemoved(TodoItemDto createdTodoItem) throws ApiException {
        List<TodoItemDto> todoItems = apiClient.getTodoItems();

        assertItemDoesNotExists(todoItems, createdTodoItem);
    }

    private void assertItemDoesNotExists(List<TodoItemDto> allTodoItems, TodoItemDto createdTodoItem) {
        assertThat(allTodoItems)
                .doesNotContain(createdTodoItem);
    }

    private DefaultApi createApiClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:" + serverPort);
        return new DefaultApi(defaultClient);
    }
}