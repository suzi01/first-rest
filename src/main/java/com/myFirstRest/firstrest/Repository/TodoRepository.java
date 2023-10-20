package com.myFirstRest.firstrest.Repository;

import com.myFirstRest.firstrest.Entity.TodoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TodoRepository extends CrudRepository <TodoEntity, UUID> {

//    @Query("SELECT b FROM TodoEntity p WHERE TodoEntity.id = :todoId")
//    TodoEntity findByTodoEntityId(@Param("userId") UUID todoId);

}
