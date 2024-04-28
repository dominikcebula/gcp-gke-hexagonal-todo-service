package com.dominikcebula.todo.service.adapter.in.rest;

import com.dominikcebula.todo.service.client.ApiClient;
import com.dominikcebula.todo.service.client.ApiException;
import com.dominikcebula.todo.service.client.ApiResponse;
import com.dominikcebula.todo.service.client.Configuration;
import com.dominikcebula.todo.service.client.api.DefaultApi;
import com.dominikcebula.todo.service.client.model.TodoItemDto;
import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.cloud.gcp.firestore.enabled=true"})
@ActiveProfiles({"test-acceptance", "env-ci"})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
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
        assertLocationHeaderReturned(apiResponse, createdTodoItem);
        assertThat(createdTodoItem).isNotNull();
        assertIdIsNotBlank(createdTodoItem);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
        assertItemWasCreated(createdTodoItem);
        assertItemWasCreatedByLocationHeader(apiResponse, createdTodoItem);
    }

    @Test
    void shouldCreteTodoItemWhenUpdateExecutedAgainstNonExistingTodoItem() throws ApiException {
        // given
        final UUID todoItemId = UUID.randomUUID();
        final String todoItemName = "TodoItem01";
        final boolean todoItemCompletedState = true;

        final TodoItemDto todoItemDto = new TodoItemDto()
                .name(todoItemName)
                .completed(todoItemCompletedState);

        // when
        ApiResponse<TodoItemDto> apiResponse = apiClient.updateTodoItemByIdWithHttpInfo(todoItemId, todoItemDto);
        TodoItemDto createdTodoItem = apiResponse.getData();

        // then
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertLocationHeaderReturned(apiResponse, createdTodoItem);
        assertThat(createdTodoItem).isNotNull();
        assertIdIsNotBlank(createdTodoItem);
        assertThat(createdTodoItem.getId()).isEqualTo(todoItemId);
        assertNameValid(createdTodoItem, todoItemName);
        assertCompletedStateValid(createdTodoItem, todoItemCompletedState);
        assertItemWasCreated(createdTodoItem);
        assertItemWasCreatedByLocationHeader(apiResponse, createdTodoItem);
    }

    @Test
    void shouldUpdateExistingTodoItemWhenUpdateExecutedAgainstExistingTodoItem() throws ApiException {
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
        assertNoLocationHeaderReturned(apiResponse);
        assertThat(retrievedUpdatedTodoItem).isNotNull();
        assertIdIsNotBlank(retrievedUpdatedTodoItem);
        assertThat(retrievedUpdatedTodoItem.getId()).isEqualTo(id);
        assertNameValid(retrievedUpdatedTodoItem, updatedTodoItem.getName());
        assertCompletedStateValid(retrievedUpdatedTodoItem, updatedTodoItem.getCompleted());
        assertThat(retrievedUpdatedTodoItem.getName()).isNotEqualTo(createdTodoItem.getName());
        assertThat(retrievedUpdatedTodoItem.getCompleted()).isNotEqualTo(createdTodoItem.getCompleted());
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

    private void assertLocationHeaderReturned(ApiResponse<TodoItemDto> apiResponse, TodoItemDto createdTodoItem) {
        assertThat(apiResponse.getHeaders()).containsEntry("Location", List.of("/todos/" + createdTodoItem.getId()));
    }

    private void assertNoLocationHeaderReturned(ApiResponse<TodoItemDto> apiResponse) {
        assertThat(apiResponse.getHeaders()).doesNotContainKey("Location");
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

    private void assertItemWasCreatedByLocationHeader(ApiResponse<TodoItemDto> apiResponse, TodoItemDto createdTodoItem) {
        Client httpClient = apiClient.getApiClient().getHttpClient();

        String location = apiResponse.getHeaders().get("Location").getFirst();

        URI uri = URI.create(getServerUrl() + location);
        TodoItemDto retrievedTodoItemDto = httpClient.target(uri).request().get(TodoItemDto.class);

        assertThat(createdTodoItem)
                .isEqualTo(retrievedTodoItemDto);
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
        defaultClient.setBasePath(getServerUrl());
        return new DefaultApi(defaultClient);
    }

    private String getServerUrl() {
        return "http://localhost:" + serverPort;
    }
}