package DataBaseConnection.demo.Task;

import DataBaseConnection.demo.Employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskServiceImpl taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<Task>> getTasksByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(taskService.getTasksByEmployeeEmail(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Task> updateTask(@PathVariable String email, @RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(email, task), HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String email, @PathVariable Long id) {
      taskService.deleteTask(email, id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
