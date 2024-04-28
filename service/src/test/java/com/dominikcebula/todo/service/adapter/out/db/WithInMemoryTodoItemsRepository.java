package com.dominikcebula.todo.service.adapter.out.db;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ActiveProfiles("in-memory-repository")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithInMemoryTodoItemsRepository {
}
