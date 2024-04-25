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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        ApiResponse<TodoItemDto> apiResponse = apiClient.createTodoItemWithHttpInfo(todoItemDto);
        TodoItemDto createdTodoItem = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(createdTodoItem).isNotNull();
        assertIdIsNotBlank(createdTodoItem);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
        assertItemWasCreated(createdTodoItem);
    }

    @Test
    void shouldGetAllTodoItems() throws ApiException {
        // when
        ApiResponse<List<TodoItemDto>> apiResponse = apiClient.getTodoItemsWithHttpInfo();
        List<TodoItemDto> allTodoItems = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(allTodoItems).isNotNull();
    }

    @Test
    void shouldGetTodoItemById() throws ApiException {
        // given
        TodoItemDto createdTodoItem = createdTodoItem();

        // when
        ApiResponse<TodoItemDto> apiResponse = apiClient.getTodoItemByIdWithHttpInfo(createdTodoItem.getId());
        TodoItemDto retrievedTodoItem = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrievedTodoItem).isEqualTo(createdTodoItem);
    }

    @Test
    void shouldGetNotFoundResponseWhenTryingToRetrieveNonExistingTodoItemById() {
        // given
        final UUID nonExistingTodoItem = UUID.randomUUID();

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> {
            apiClient.getTodoItemByIdWithHttpInfo(nonExistingTodoItem);
        });

        // then
        assertThat(apiException.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(apiException.getResponseBody()).isNull();
    }

    @Test
    void shouldGetNoContentResponseWhenDeletingExistingItem() throws ApiException {
        // given
        TodoItemDto createdTodoItem = createdTodoItem();

        // when item is deleted
        ApiResponse<Void> apiResponse = apiClient.deleteTodoItemByIdWithHttpInfo(createdTodoItem.getId());

        // then verify item does not exist
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertItemWasRemoved(createdTodoItem);
    }

    @Test
    void shouldGetNotFoundResponseWhenDeletingNonExistingItem() {
        // when
        ApiException apiException = assertThrows(ApiException.class, () -> {
            apiClient.deleteTodoItemByIdWithHttpInfo(UUID.randomUUID());
        });

        // then
        assertThat(apiException.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(apiException.getResponseBody()).isNull();
    }

    private TodoItemDto createdTodoItem() throws ApiException {
        TodoItemDto todoItemDto = new TodoItemDto()
                .name("TodoItem01")
                .completed(true);

        return apiClient.createTodoItem(todoItemDto);
    }

    private void assertIdIsNotBlank(TodoItemDto createdTodoItem) {
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