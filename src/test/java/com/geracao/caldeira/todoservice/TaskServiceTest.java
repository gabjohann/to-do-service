package com.geracao.caldeira.todoservice;

import com.geracao.caldeira.todoservice.model.Task;
import com.geracao.caldeira.todoservice.repository.TaskRepository;
import com.geracao.caldeira.todoservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> taskList = new ArrayList<>();

        taskList.add(new Task("estudar java", new Date(), false));
        taskList.add(new Task("estudar spring", new Date(), true));

        // mock dos dados para retornar a lista de tarefas
        Mockito.when(taskRepository.getAllTasks()).thenReturn(taskList);

        List<Task> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testAddTask() {
        Task newTask = new Task("estudar java", new Date(), false);

        Mockito.when(taskRepository.addNewTask(newTask)).thenReturn(newTask);

        Task addedTask = taskService.addTask(newTask);

        assertNotNull(addedTask);
        assertEquals("estudar java", addedTask.getDescription());
    }

    @Test
    public void testEditTask() {
        Task existingTask = new Task("exemplo de tarefa já existente", new Date(), false);
        existingTask.setTaskId(1L);

        // Configure o mock para retornar a tarefa existente e para atualizá-la
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        Mockito.when(taskRepository.addNewTask(existingTask)).thenReturn(existingTask);

        Map<String, Object> updates = new HashMap<>();
        updates.put("description", "exemplo de tarefa atualizada");
        updates.put("completed", true);

        // Chame o método a ser testado para atualizar a conclusão
        Task updatedTask = new Task(
                (String) updates.get("description"),
                existingTask.getDueDate(),
                (Boolean) updates.get("completed")
        );
        updatedTask.setTaskId(existingTask.getTaskId());

        Task editedTask = taskService.editTask(1L, updatedTask);

        assertNotNull(editedTask);
        assertEquals("exemplo de tarefa atualizada", editedTask.getDescription());
        assertTrue(editedTask.isCompleted());
    }

    @Test
    public void testDeleteTask() {
        long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task("tarefa a ser deletada", new Date(), false)));

        taskService.deleteTask(taskId);

        Mockito.verify(taskRepository).delete(Mockito.any(Task.class));
    }

    @Test
    public void testFindTaskById() {
        long taskId = 1L;

        Task expectedTask = new Task("tarefa a ser encontrada", new Date(), false);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));

        Optional<Task> foundTask = taskService.findTaskById(taskId);

        assertTrue(foundTask.isPresent());
        assertEquals(expectedTask, foundTask.get());
    }
}
