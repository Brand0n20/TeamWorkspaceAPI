package DataBaseConnection.demo.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    TaskServiceImpl taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }
}
