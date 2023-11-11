package DataBaseConnection.demo.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();

    Task getTaskById(Long id);

    List<Task> getTasksByEmployeeEmail(String email);

    Task saveTask(Task task);

    Task updateTask(String email, Task task);

    void deleteTask(String email, Long id);

}
