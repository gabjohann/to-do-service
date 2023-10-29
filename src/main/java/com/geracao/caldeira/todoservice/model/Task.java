package com.geracao.caldeira.todoservice.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Task {
    private static final AtomicLong idCounter = new AtomicLong(0L);

    private long taskId;

    @NotEmpty(message = "Informe uma descrição para a sua tarefa!")
    private String description;

    @NotNull(message = "Digite uma data de vencimento!")
    @FutureOrPresent(message = "Digite uma data de vencimento válida!")
    private Date dueDate;

    @AssertFalse(message = "A tarefa não pode estar marcada como concluída!")
    private boolean completed;

    public Task() {
        this.taskId = idCounter.incrementAndGet();
    }
}
