package com.geracao.caldeira.todoservice.service;

import com.geracao.caldeira.todoservice.model.Task;
import com.geracao.caldeira.todoservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public Task addTask(Task task) {
        return taskRepository.addNewTask(task);
    }

    public Task editTask(Long taskId, Task updatedTask) {

        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            Task taskToUpdate = existingTask.get();
            taskToUpdate.setDescription(updatedTask.getDescription());
            taskToUpdate.setDueDate(updatedTask.getDueDate());
            taskToUpdate.setCompleted(updatedTask.isCompleted());

            return taskRepository.addNewTask(taskToUpdate);
        } else {
            // Lida com o caso em que a tarefa não existe
            return null;
        }
    }

    public void deleteTask(long taskId) {
        Optional<Task> taskToDelete = taskRepository.findById(taskId);
        taskToDelete.ifPresent(taskRepository::delete);
    }

    public Optional<Task> findTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
}
