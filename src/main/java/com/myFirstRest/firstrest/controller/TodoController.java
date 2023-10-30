package com.myFirstRest.firstrest.controller;

import com.myFirstRest.firstrest.Exceptions.TodoBadRequest;
import com.myFirstRest.firstrest.Exceptions.TodoNotFoundException;
import com.myFirstRest.firstrest.Model.PartialTodoDTO;
import com.myFirstRest.firstrest.Model.TodoDTO;
import com.myFirstRest.firstrest.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class TodoController {

    @Autowired
    private TodoService todoService;

    //    Return all todos from the todos.json file
    @GetMapping("/todos")
    public ResponseEntity<List<TodoDTO>> getAllTodos(){
        List<TodoDTO> todoDTOList = todoService.getAllTodos();
        return new ResponseEntity<>(todoDTOList, HttpStatus.OK);
    }


    //    Return a specific todo with the corresponding id
    @GetMapping("/todos/{todoId}")
    public  ResponseEntity<TodoDTO> getTodo(@PathVariable("todoId") UUID todoId){
        TodoDTO todoEntity = todoService.getTodoById(todoId);
        return new ResponseEntity<>( todoEntity, HttpStatus.OK);
    }

    //    Return a list of overdue todos or an empty list if there are no overdue todos.
    //    Todos can be filtered based on their due date attribute
    @GetMapping("/todos/overdue")
    public ResponseEntity<List<TodoDTO>> getAllOverdueTodos(){
        List<TodoDTO> todoDTOList = todoService.getOverdueTodos();
        return new ResponseEntity<>(todoDTOList, HttpStatus.OK);
    }


    //    Return a list of completed todos or an empty list, if no todos have been completed.
    //    Todos can be filtered based on their due date attribute.
    @GetMapping("/todos/completed")
    public ResponseEntity<List<TodoDTO>> getAllCompletedTodos(){
        List<TodoDTO> todoDTOList = todoService.getCompletedTodos();
        return new ResponseEntity<>(todoDTOList, HttpStatus.OK);
    }

    //    Add a new todo to the todo list
    @PostMapping("/todos")
    public ResponseEntity<TodoDTO> addTodo(@Valid @RequestBody TodoDTO todoDTO){
        todoDTO = todoService.saveTodo(todoDTO);
        return new ResponseEntity<>(todoDTO, HttpStatus.CREATED);
    }

    //    Edit the name and/or due date attributes of a todo.
    @PatchMapping("/todos/{todoId}")
    public ResponseEntity<TodoDTO> editTodo(@PathVariable("todoId") UUID todoId, @Valid @RequestBody PartialTodoDTO todoDTO){
        TodoDTO updatedTodo = todoService.updateDTO(todoId, todoDTO);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);

    }

    //    Update todo, set attribute complete to true
    @PostMapping("/todos/{todoId}/complete")
    public ResponseEntity<TodoDTO> updateTodoTrue(@PathVariable("todoId") UUID todoId){
        TodoDTO completedTodo = todoService.updateToCompleteDTO(todoId);
        return new ResponseEntity<>(completedTodo, HttpStatus.OK);

    }

    //    Update todo, set attribute complete to false
    @PostMapping("/todos/{todoId}/undo")
    public ResponseEntity<TodoDTO> updateTodoFalse(@PathVariable("todoId") UUID todoId){
        TodoDTO notCompleteDTO = todoService.updateToNotCompleteDTO(todoId);
        return new ResponseEntity<>(notCompleteDTO, HttpStatus.OK);
    }

    //    Deletes a todo by id
    @DeleteMapping("/todos/{todoId}")
    public void deleteTodo(@PathVariable("todoId") UUID todoId){
        todoService.deleteTodo(todoId);
        System.out.println("Todo has been deleted!");
    }

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleTodoNotFoundException(TodoNotFoundException todoNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(todoNotFoundException.getMessage());
    }

    @ExceptionHandler(TodoBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTodoBadRequestException(TodoBadRequest todoBadRequest){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(todoBadRequest.getMessage());
    }


}

