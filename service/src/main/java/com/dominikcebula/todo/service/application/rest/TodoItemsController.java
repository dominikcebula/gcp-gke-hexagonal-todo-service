package com.dominikcebula.todo.service.application.rest;

import com.dominikcebula.todo.service.api.TodosApi;
import com.dominikcebula.todo.service.model.TodoItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoItemsController implements TodosApi {
    @Override
    public ResponseEntity<List<TodoItem>> getTodoItems() {
        return ResponseEntity.ok().body(List.of());
    }
}
