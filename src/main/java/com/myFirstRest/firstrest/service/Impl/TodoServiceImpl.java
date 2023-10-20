package com.myFirstRest.firstrest.service.Impl;

import com.myFirstRest.firstrest.Converter.TodoConverter;
import com.myFirstRest.firstrest.Entity.TodoEntity;
import com.myFirstRest.firstrest.Exceptions.TodoBadRequest;
import com.myFirstRest.firstrest.Exceptions.TodoNotFoundException;
import com.myFirstRest.firstrest.Model.PartialTodoDTO;
import com.myFirstRest.firstrest.Model.TodoDTO;
import com.myFirstRest.firstrest.Repository.TodoRepository;
import com.myFirstRest.firstrest.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoConverter todoConverter;

    @Override
    public List<TodoDTO> getAllTodos() {
        List<TodoEntity> listOfTodos = (List<TodoEntity>) todoRepository.findAll();
        List<TodoDTO> todosList = new ArrayList<>();
        for(TodoEntity te: listOfTodos){
            TodoDTO dto = todoConverter.convertyEntityToDTO(te);
            todosList.add(dto);
        }
        return todosList;
    }

    @Override
    public TodoDTO saveTodo(TodoDTO todoDTO) {
        try {
            TodoEntity todoEntity  = todoConverter.convertyDTOToEntity(todoDTO);
            todoEntity = todoRepository.save(todoEntity);
            todoDTO = todoConverter.convertyEntityToDTO(todoEntity);
        } catch (Exception ex){
            throw new TodoBadRequest("Todo type is incorrect, please make sure it has the correct attributes");
        }
        return todoDTO;
    }

    @Override
    public TodoDTO getTodoById(UUID todoId) {
       Optional<TodoEntity> optionalUserEntity = todoRepository.findById(todoId);
        TodoDTO todoDto;
        if(optionalUserEntity.isPresent()){
            TodoEntity todoEntity = optionalUserEntity.get();
            todoDto = todoConverter.convertyEntityToDTO(todoEntity);
        } else {
            throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
        }
        return todoDto;
    }

    @Override
    public List<TodoDTO> getOverdueTodos() {
        List<TodoEntity> listOfTodos = (List<TodoEntity>) todoRepository.findAll();
        List<TodoDTO> todosList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for(TodoEntity te: listOfTodos){
            if(!te.isCompleted() && te.getDue().toLocalDate().isBefore(currentDate)){
                TodoDTO dto = todoConverter.convertyEntityToDTO(te);
                todosList.add(dto);
            }
        }
        return todosList;
    }

    @Override
    public List<TodoDTO> getCompletedTodos() {
        List<TodoEntity> todoEntityList = (List<TodoEntity>) todoRepository.findAll();
        List<TodoDTO> todoDTOList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();

        for(TodoEntity todoEntity: todoEntityList){
            if(todoEntity.isCompleted() && todoEntity.getDue().toLocalDate().isBefore(localDate)){
                TodoDTO todoDTO = todoConverter.convertyEntityToDTO(todoEntity);
                todoDTOList.add(todoDTO);
            }
        }
        return todoDTOList;
    }

    @Override
    public TodoDTO updateToCompleteDTO(UUID todoId) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(todoId);
        TodoDTO completedDto = null;
        if(todoEntity.isPresent()){
            TodoEntity te = todoEntity.get();
            te.setCompleted(true);
            completedDto = todoConverter.convertyEntityToDTO(te);
            todoRepository.save(te);
        } else {
            throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
        }
        return completedDto;
    }

    @Override
    public TodoDTO updateToNotCompleteDTO(UUID todoId) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(todoId);
        TodoDTO notCompletedDto;
        if(todoEntity.isPresent()){
            TodoEntity te = todoEntity.get();
            te.setCompleted(false);
            notCompletedDto = todoConverter.convertyEntityToDTO(te);
            todoRepository.save(te);
        }
        else {
            throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
        }
        return notCompletedDto;
    }

    @Override
    public void deleteTodo(UUID todoId) {
//        todoRepository.deleteById(todoId);
        Optional<TodoEntity> todoEntity = todoRepository.findById(todoId);
        if(todoEntity.isPresent()){
            todoRepository.deleteById(todoId);
        }
        else {
            throw new TodoNotFoundException("Todo with ID " + todoId + " does not exist");
        }
    }

    @Override
    public TodoDTO updateDTO(UUID todoID, PartialTodoDTO partialTodoDTO) {
        Optional<TodoEntity> optionalTodo = todoRepository.findById(todoID);
        TodoDTO updatedDto = null;
        if(optionalTodo.isPresent()){
            TodoEntity te = optionalTodo.get();
            if(partialTodoDTO.getName() != null){
                te.setName(partialTodoDTO.getName());
            }
            if(partialTodoDTO.getDue() != null){
                te.setDue(partialTodoDTO.getDue());
            }
            updatedDto = todoConverter.convertyEntityToDTO(te);
            todoRepository.save(te);
        } else {
            throw new TodoNotFoundException("Todo with ID " + todoID + " not found");
        }
        return updatedDto;
    }
}

