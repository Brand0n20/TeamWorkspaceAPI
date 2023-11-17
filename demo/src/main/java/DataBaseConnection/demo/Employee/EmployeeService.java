package DataBaseConnection.demo.Employee;

import DataBaseConnection.demo.Task.Task;

import java.util.List;

public interface EmployeeService {

    void addTaskToEmployee(String email, String name);
    Employee getEmployee(String email);
    List<Employee> getEmployees();

    List<Task> getEmployeeTasks(String email);

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(String email, Employee employee);

    void deleteEmployee(String email);

}
