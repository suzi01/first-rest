package com.myFirstRest.firstrest.Model;


import com.sun.istack.NotNull;
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
    @NotNull
    private String name;
    @NotNull
    private LocalDateTime created;
    private LocalDateTime due;
    private boolean completed;
}
