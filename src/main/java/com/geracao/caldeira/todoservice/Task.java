package com.geracao.caldeira.todoservice;

import lombok.Data;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Task {
    private static final AtomicLong idCounter = new AtomicLong(0L);

    private long taskId;
    private String description;
    private Date dueDate;
    private boolean completed;

    public Task() {
        this.taskId = idCounter.incrementAndGet();
    }
}
