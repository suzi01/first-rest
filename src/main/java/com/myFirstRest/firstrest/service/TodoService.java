package com.myFirstRest.firstrest.service;

import com.myFirstRest.firstrest.Model.PartialTodoDTO;
import com.myFirstRest.firstrest.Model.TodoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TodoService {

    List<TodoDTO> getAllTodos();
    TodoDTO saveTodo(TodoDTO todoDTO);

    TodoDTO getTodoById(UUID todoId);

    List<TodoDTO> getOverdueTodos();

    List<TodoDTO>getCompletedTodos();

    TodoDTO updateToCompleteDTO(UUID todoId);

    TodoDTO updateToNotCompleteDTO(UUID todoId);

    void deleteTodo(UUID todoId);

    TodoDTO updateDTO(UUID todoID, PartialTodoDTO todoDTO);
}
