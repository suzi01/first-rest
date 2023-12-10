package com.myFirstRest.firstrest.ControllerTest;

import com.myFirstRest.firstrest.Exceptions.TodoNotFoundException;
import com.myFirstRest.firstrest.Model.PartialTodoDTO;
import com.myFirstRest.firstrest.Model.TodoDTO;
import com.myFirstRest.firstrest.controller.TodoController;
import com.myFirstRest.firstrest.service.TodoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
public class TodoControllerTest {



//    @Test
//    public void testGetTodoById() {
//        UUID todoId = UUID.randomUUID();
//        LocalDateTime futureDate = LocalDateTime.now().plusYears(1);
//        TodoDTO expectedTodoDTO = new TodoDTO(
//                todoId,
//                "Learn Fundamentals of Java",
//                LocalDateTime.now(),
//                futureDate,
//                false);
//        when(todoService.getTodoById(todoId)).thenReturn(expectedTodoDTO);
//
//        ResponseEntity<TodoDTO> responseEntity = todoController.getTodo(todoId);
//
//        assertNotNull(responseEntity);
//
//        TodoDTO actualTodo = responseEntity.getBody();
//        assertNotNull(actualTodo);
//        assertEquals(expectedTodoDTO.getId(), actualTodo.getId());
//        assertEquals(expectedTodoDTO.getName(), actualTodo.getName());
//        assertEquals(expectedTodoDTO.getDue(), actualTodo.getDue());
//        assertEquals(expectedTodoDTO.getCreated(), actualTodo.getCreated());
//        assertEquals(expectedTodoDTO.isCompleted(), actualTodo.isCompleted());
//    }

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TodoService todoService;

    @BeforeEach
    void setUp(){
        TodoController todoController = new TodoController();
        ReflectionTestUtils.setField(todoController, "todoService", todoService);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    public static final String invalidTodoJson = "{\"completed\": true}";
    public static final String partialTodoJson = "{\"name\":\"Renew Passport Again\", \"due\":\"2028-02-20T18:25:43.511\"}";



    @Test
    public void testGetTodoById() throws Exception {
        UUID todoId = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 7, 14, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(2024, 9, 14, 0, 0);
        TodoDTO expectedTodoDTO = new TodoDTO(
                todoId,
                "Learn Fundamentals of Java",
                currentDateTime,
                futureDate,
                false);
        when(todoService.getTodoById(todoId)).thenReturn(expectedTodoDTO);

        mockMvc.perform(get("/todos/" + todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId.toString()))
                .andExpect(jsonPath("$.name").value("Learn Fundamentals of Java"))
                .andExpect(jsonPath("$.created", Matchers.is(List.of(2023, 7, 14, 0, 0))))
                .andExpect(jsonPath("$.due", Matchers.is(List.of(2024, 9, 14, 0, 0))))
                .andExpect(jsonPath("$.completed").value(false))
                .andReturn()
                .getResponse();
    }

    @Test
    public void testTodoIdDoesNotExistForGetRequest() throws Exception {
        UUID nonExistentTodoId = UUID.randomUUID();
        when(todoService.getTodoById(nonExistentTodoId)).thenThrow(new TodoNotFoundException("Todo with ID " + nonExistentTodoId + " not found"));
        mockMvc.perform(get("/todos/" + nonExistentTodoId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID " + nonExistentTodoId + " not found"));
    }

    @Test //needs work, need to create an irregular todo
    public void testTodoIdDoesNotExistForPostRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" )
                        .content(invalidTodoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testTodoIdDoesNotExistForPostCompleteRequest() throws Exception {
        UUID nonExistentTodoId = UUID.randomUUID();
        when(todoService.updateToCompleteDTO(nonExistentTodoId)).thenThrow(new TodoNotFoundException("Todo with ID " + nonExistentTodoId + " not found"));
        mockMvc.perform(post("/todos/" + nonExistentTodoId + "/complete").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID " + nonExistentTodoId + " not found"));
    }

    @Test
    public void testTodoIdDoesNotExistForPostUndoRequest() throws Exception {
        UUID nonExistentTodoId = UUID.randomUUID();
        when(todoService.updateToNotCompleteDTO(nonExistentTodoId)).thenThrow(new TodoNotFoundException("Todo with ID " + nonExistentTodoId + " not found"));
        mockMvc.perform(post("/todos/" + nonExistentTodoId + "/undo").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID " + nonExistentTodoId + " not found"));
    }


    @Test //need to change
    public void testTodoIdDoesExistForPatchRequest() throws Exception {
        UUID partialTodoId = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 7, 14, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(2024, 9, 14, 0, 0);
        PartialTodoDTO partialTodoDTO = new PartialTodoDTO(
            "Learn Fundamentals of Java",
            futureDate);
        TodoDTO expectedTodoDTO = new TodoDTO(
            partialTodoId,
            "Learn Fundamentals of Java",
            currentDateTime,
            futureDate,
            false);
        when(todoService.updateDTO(any(UUID.class), any(PartialTodoDTO.class))).thenReturn(expectedTodoDTO);
        mockMvc.perform(patch("/todos/" + partialTodoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(partialTodoJson))
            .andExpect(status().isOk());
    }

    @Test //need to change
    public void testTodoIdDoesNotExistForPatchRequest() throws Exception {
        UUID partialTodoId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.of(2024, 9, 14, 0, 0);
        PartialTodoDTO partialTodoDTO = new PartialTodoDTO(
                "Learn Fundamentals of Java",
                futureDate);
        when(todoService.updateDTO(any(UUID.class), any(PartialTodoDTO.class))).thenThrow(new TodoNotFoundException("Todo with ID " + partialTodoId + " not found"));
        mockMvc.perform(patch("/todos/" + partialTodoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(partialTodoJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID " + partialTodoId + " not found"));
    }


    @Test
    public void testTodoIdDoesNotExistForDeleteRequest() throws Exception {
        UUID nonExistentTodoId = UUID.randomUUID();
        doThrow(new TodoNotFoundException("Todo with ID " + nonExistentTodoId + " does not exist") )
            .when(todoService).deleteTodo(nonExistentTodoId);
        mockMvc.perform(delete("/todos/" +nonExistentTodoId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID " + nonExistentTodoId + " does not exist"));
    }



}
