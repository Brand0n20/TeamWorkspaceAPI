package DataBaseConnection.demo.Task;

import DataBaseConnection.demo.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

}
