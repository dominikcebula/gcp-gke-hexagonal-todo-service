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
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.cloud.gcp.firestore.enabled=true"})
@ActiveProfiles({"test-acceptance", "env-ci"})
class TodosApiAcceptanceTest {
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
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createdTodoItem).isNotNull();
        assertIdIsNotBlank(createdTodoItem);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
    }

    @Test
    void shouldUpdateTodoItem() throws ApiException {
        // given
        TodoItemDto createdTodoItem = createdTodoItem();
        UUID id = createdTodoItem.getId();

        TodoItemDto updatedTodoItem = new TodoItemDto();
        updatedTodoItem.setName("TodoItem02");
        updatedTodoItem.setCompleted(false);

        // when
        ApiResponse<TodoItemDto> apiResponse = apiClient.updateTodoItemByIdWithHttpInfo(id, updatedTodoItem);
        TodoItemDto retrievedUpdatedTodoItem = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrievedUpdatedTodoItem).isNotNull();
        assertThat(retrievedUpdatedTodoItem.getId()).isEqualTo(id);
        assertNameValid(retrievedUpdatedTodoItem, updatedTodoItem.getName());
        assertCompletedStateValid(retrievedUpdatedTodoItem, updatedTodoItem.getCompleted());
    }

    @Test
    void shouldGetAllTodoItems() throws ApiException {
        // given
        var atLeastOneCreatedTodoItem = createdTodoItem();

        // when
        ApiResponse<List<TodoItemDto>> apiResponse = apiClient.getTodoItemsWithHttpInfo();
        List<TodoItemDto> allTodoItems = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(allTodoItems)
                .isNotEmpty()
                .contains(atLeastOneCreatedTodoItem);
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
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrievedTodoItem).isEqualTo(createdTodoItem);
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

    private DefaultApi createApiClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(getServerUrl());
        return new DefaultApi(defaultClient);
    }

    private String getServerUrl() {
        return "http://localhost:" + serverPort;
    }
}