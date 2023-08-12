package DataBaseConnection.demo.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepo employeeRepo;

    @PostMapping("/addEmployee")
    public void addEmployee(@RequestBody Employee employee) {
        employeeRepo.save(employee);
    }
}
