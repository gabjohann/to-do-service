package com.geracao.caldeira.todoservice.service;

import com.geracao.caldeira.todoservice.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final List<Task> taskList;

    public TaskService() {
        taskList = new ArrayList<>();
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public Task addTask(Task task) {
        taskList.add(task);
        return task;
    }

    public Task editTask(Long taskId, Task updatedTask) {
        findTaskById(taskId).ifPresent(existingTask -> {
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setDueDate(updatedTask.getDueDate());
            existingTask.setCompleted(updatedTask.isCompleted());
        });
        return updatedTask;
    }

    public void deleteTask(long taskId) {
        findTaskById(taskId).ifPresent(taskList::remove);
    }

    public Optional<Task> findTaskById(Long taskId) {
        return taskList.stream().filter(task -> task.getTaskId() == taskId).findFirst();
    }
}
