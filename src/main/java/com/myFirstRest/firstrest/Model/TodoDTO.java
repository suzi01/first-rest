package com.myFirstRest.firstrest.Model;


import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private UUID id;
    @NotNull (message = "The name is required.")
    private String name;
    @NotNull (message = "The created date is required.")
    private LocalDateTime created;
    private LocalDateTime due;
    private boolean completed;
}
