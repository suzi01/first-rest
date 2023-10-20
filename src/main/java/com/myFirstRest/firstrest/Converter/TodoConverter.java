package com.myFirstRest.firstrest.Converter;

import com.myFirstRest.firstrest.Entity.TodoEntity;
import com.myFirstRest.firstrest.Model.TodoDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TodoConverter {
    public TodoDTO convertyEntityToDTO(TodoEntity todoEntity){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(todoEntity.getId());
        todoDTO.setName(todoEntity.getName());
        todoDTO.setCreated(todoEntity.getCreated());
        todoDTO.setDue(todoEntity.getDue());
        todoDTO.setCompleted(todoEntity.isCompleted());
        return todoDTO;
    }

    public TodoEntity convertyDTOToEntity(TodoDTO todoDTO){
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setName(todoDTO.getName());
        todoEntity.setCreated(todoDTO.getCreated());
        todoEntity.setDue(todoDTO.getDue());
        todoEntity.setCompleted(todoDTO.isCompleted());
        return todoEntity;
    }
}
