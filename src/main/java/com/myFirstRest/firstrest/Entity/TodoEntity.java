package com.myFirstRest.firstrest.Entity;

import com.sun.istack.NotNull;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="TODO_TABLE")
@Getter
@Setter
@NoArgsConstructor
public class TodoEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private LocalDateTime created;
    private LocalDateTime due;
    private boolean completed;
}
