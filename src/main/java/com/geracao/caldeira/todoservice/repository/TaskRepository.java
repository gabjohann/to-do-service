package com.geracao.caldeira.todoservice.repository;

import com.geracao.caldeira.todoservice.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    private final List<Task> taskList;

    public TaskRepository() {
        taskList = new ArrayList<>();
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public Task addNewTask(Task task) {
        taskList.add(task);
        return task;
    }

    public void delete(Task task) {
        taskList.remove(task);
    }

    public void clear() {
        taskList.clear();
    }

    public Optional<Task> findById(Long taskId) {
        return taskList.stream().filter(task -> task.getTaskId() == taskId).findFirst();
    }
}
