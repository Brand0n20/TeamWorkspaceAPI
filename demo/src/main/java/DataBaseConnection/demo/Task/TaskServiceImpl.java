package DataBaseConnection.demo.Task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService{

    @Autowired
    TaskRepo taskRepo;

    @Override
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepo.findById(id).orElse(null);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }
}
