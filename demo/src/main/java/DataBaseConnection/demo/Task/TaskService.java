package DataBaseConnection.demo.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();

    Task getTaskById(Long id);

    Task saveTask(Task task);
}
