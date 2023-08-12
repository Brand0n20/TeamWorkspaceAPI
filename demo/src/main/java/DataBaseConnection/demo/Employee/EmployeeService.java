package DataBaseConnection.demo.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(Employee employee);

    void addTaskToUser(String email, String name);
    Employee getEmployee(String email);
    List<Employee> getEmployees();
}
