package com.geracao.caldeira.todoservice.controller;

import com.geracao.caldeira.todoservice.exception.CustomError;
import com.geracao.caldeira.todoservice.exception.ErrorResponse;
import com.geracao.caldeira.todoservice.model.Task;
import com.geracao.caldeira.todoservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    ErrorResponse errorResponse = new ErrorResponse();
    CustomError customError = new CustomError();

    // listar todas as tarefas
    @GetMapping("/")
    public List<Task> getAllTaskList() {
        return taskService.getAllTasks();
    }

    // adicionar uma nova tarefa
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task, BindingResult result) {

        if (result.hasErrors()) {
            errorResponse.setMessage("Requisição possui campos inválidos");
            errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());

            List<CustomError> postErrorList = new ArrayList<>();

            // Adicione mensagens de erro personalizadas à lista de erros.
            result.getFieldErrors().forEach(fieldError -> {
                CustomError customError = new CustomError();
                customError.setMessage(fieldError.getDefaultMessage());
                postErrorList.add(customError);
                // errorResponse.getErrorList().add(customError);
            });

            errorResponse.setErrorList(postErrorList);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        taskService.addTask(task);

        return ResponseEntity.ok("Tarefa adicionada com sucesso!");
    }

    @PutMapping("/edit/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId, @RequestBody Map<String, Object> updates) {
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
                    errorResponse.setMessage("Erro ao editar a tarefa");
                    errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

                    customError.setMessage("Erro ao editar o campo: " + field);
                    errorResponse.getErrorList().add(customError);

                    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            taskService.editTask(taskId, existingTask);

            return ResponseEntity.ok(existingTask);
        } else {
            errorResponse.setMessage("Tarefa " + taskId + " não encontrada");
            errorResponse.setCode(HttpStatus.NOT_FOUND.value());
            errorResponse.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // remover uma tarefa
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        Optional<Task> taskToDelete = taskService.findTaskById(taskId);
        if (taskToDelete.isPresent()) {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Tarefa excluída com sucesso!");
        } else {
            errorResponse.setMessage("Tarefa não encontrada");
            errorResponse.setCode(HttpStatus.NOT_FOUND.value());
            errorResponse.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
