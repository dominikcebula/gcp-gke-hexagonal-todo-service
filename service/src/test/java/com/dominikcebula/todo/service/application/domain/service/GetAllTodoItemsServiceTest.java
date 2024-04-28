package com.dominikcebula.todo.service.application.domain.service;

import com.dominikcebula.todo.service.adapter.out.db.WithInMemoryTodoItemsRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithInMemoryTodoItemsRepository
@DirtiesContext
class GetAllTodoItemsServiceTest {

}