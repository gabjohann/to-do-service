package com.geracao.caldeira.todoservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final List<Task> taskList;

    // listar todas as tarefas
    @GetMapping("/tasks")
    public List<Task> getAllTaskList() {
        return taskList;
    }

    // adicionar uma nova tarefa
    @PostMapping("/tasks/add")
    public Task addTask(@RequestBody Task task) {
        taskList.add(task);
        return task;
    }

    // editar uma tarefa
    @PutMapping("/tasks/edit/{taskId}")
    public Task editTask(@PathVariable Long taskId, @RequestBody Map<String, Object> updates) {
        Optional<Task> taskToEdit = findTaskById(taskId);

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
    @DeleteMapping("/tasks/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        Optional<Task> taskToDelete = findTaskById(taskId);
        if (taskToDelete.isPresent()) {
            taskList.remove(taskToDelete.get());
        } else {
            throw new TaskNotFoundException("Tarefa " + taskId + " não encontrada.");
        }
    }

    private Optional<Task> findTaskById(Long taskId) {
        return taskList.stream().filter(task -> task.getTaskId() == taskId).findFirst();
    }
}
