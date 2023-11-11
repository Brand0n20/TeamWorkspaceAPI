package DataBaseConnection.demo.Employee;

import java.util.List;

public interface EmployeeService {

    void addTaskToEmployee(String email, String name);
    Employee getEmployee(String email);
    List<Employee> getEmployees();

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(String email, Employee employee);

    void deleteEmployee(String email);

}
