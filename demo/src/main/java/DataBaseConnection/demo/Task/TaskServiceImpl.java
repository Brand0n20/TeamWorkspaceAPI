package DataBaseConnection.demo.Task;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeRepo;
import Exceptions.BadRequest;
import Exceptions.ResourceNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public List<Task> getTasks() {
        try {
            log.info("Fetching all tasks");
            return taskRepo.findAll();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepo.findById(id);
        if (task.isEmpty()) {
            log.error("Task with id of " + id + " doesn't exist" );
            throw new ResourceNotFound("Task with this id doesn't exist");
        }
            try {
                return task.get();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new NoSuchElementException(e.getMessage());
            }

    }

    @Override
    public List<Task> getTasksByEmployeeEmail(String email) {
        List<Task> tasks;
            try {
                tasks = taskRepo.findTasksByEmployeeEmail(email);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new NoSuchElementException(e.getMessage());
            }
            if (tasks.isEmpty()) {
                log.error("No tasks assigned to this user yet");
                throw new ResourceNotFound("No tasks with this email exist");
            } else {
                return tasks;
            }

    }


    @Override
    public Task saveTask(Task task) {
        Employee existingEmployee;
        try {
            existingEmployee = employeeRepo.findByEmail(task.getEmployeeEmail());
            if (existingEmployee != null) {
                task.setEmployee(existingEmployee);
                taskRepo.save(task);
            } else {
                throw new ResourceNotFound("Employee email wasn't provided");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return task;
    }

    @Override
    public Task updateTask(Long id, Task task) {

        Task existingTask = taskRepo.findById(id).orElse(null);
        Employee updatedTaskEmployee = employeeRepo.findByEmail(task.getEmployeeEmail());
        if (existingTask == null) {
            log.error("Task under this id doesn't exist");
            throw new ResourceNotFound("Task under this id doesn't exist");
        } else {
            if (task.getId() == null) {
                task.setId(existingTask.getId());
            }
            if (updatedTaskEmployee != null && task.getId() == existingTask.getId()) {
                try {
                    task = taskRepo.save(task);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            } else if (updatedTaskEmployee == null){
                log.error("Task employee with this email doesn't exist");
                throw new ResourceNotFound("Task employee with this email doesn't exist");
            } else if (task.getId() != existingTask.getId()) {
                log.error("Task id must match existing task id");
                throw new BadRequest("Task id must match existing task id");
            }
        }
        return task;
    }

    @Override
    public void deleteTask(Long id) {
            Task task = taskRepo.findById(id).orElse(null);
            if (task.getId() != null) {
                    try {
                        taskRepo.delete(task);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
            } else {
                log.error("Task with this id doesn't exist");
                throw new BadRequest("Task with this id doesn't exist");
            }
        }

}
