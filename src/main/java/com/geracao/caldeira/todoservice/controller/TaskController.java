package com.geracao.caldeira.todoservice.controller;

import com.geracao.caldeira.todoservice.TaskNotFoundException;
import com.geracao.caldeira.todoservice.model.Task;
import com.geracao.caldeira.todoservice.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // listar todas as tarefas
    @GetMapping("/")
    public List<Task> getAllTaskList() {
        return taskService.getAllTasks();
    }

    // adicionar uma nova tarefa
    @PostMapping("/add")
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @PutMapping("/edit/{taskId}")
    public Task editTask(@PathVariable Long taskId, @RequestBody Map<String, Object> updates) {
        Optional<Task> taskToEdit = taskService.findTaskById(taskId);

        if (taskToEdit.isPresent()) {
            Task existingTask = taskToEdit.get();

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                try {
                    Field taskField = Task.class.getDeclaredField(field);
                    taskField.setAccessible(true);
                    taskField.set(existingTask, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // tratar os erros!!!
                }
            }
            return existingTask;
        } else {
            throw new TaskNotFoundException("Tarefa " + taskId + " não encontrada.");
        }
    }

    // remover uma tarefa
    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        Optional<Task> taskToDelete = taskService.findTaskById(taskId);
        if (taskToDelete.isPresent()) {
            taskService.deleteTask(taskId);
        } else {
            throw new TaskNotFoundException("Tarefa " + taskId + " não encontrada.");
        } // está retornando erro 500, melhorar a tratativa de erros
    }
}
