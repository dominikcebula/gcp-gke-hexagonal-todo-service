package com.dominikcebula.todo.service.adapter.out.db;

import com.dominikcebula.todo.service.application.domain.model.TodoItem;
import com.dominikcebula.todo.service.application.port.out.RepositoryException;
import com.dominikcebula.todo.service.application.port.out.TodoItemsRepository;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class FirestoreTodoItemsRepository implements TodoItemsRepository {
    private final Firestore firestore;

    @Override
    public void save(TodoItem todoItem) {
        try {
            getTodosCollection().document(todoItem.getId().toString()).set(todoItem).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RepositoryException("Error occurred while storing todo item.", e);
        }
    }

    @Override
    public List<TodoItem> getAll() {
        try {
            return getTodosCollection().get().get().getDocuments().stream()
                    .map(document -> document.toObject(TodoItem.class))
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RepositoryException("Error occurred while fetching all todo items from db.", e);
        }
    }

    private CollectionReference getTodosCollection() {
        return firestore.collection("todos");
    }
}
