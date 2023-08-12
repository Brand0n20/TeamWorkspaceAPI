package DataBaseConnection.demo;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeService;
import DataBaseConnection.demo.Role.Role;
import DataBaseConnection.demo.Task.Task;
import DataBaseConnection.demo.Task.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config {

    @Bean
    CommandLineRunner runEmployee(EmployeeService employeeService) {
        return args -> {

            Role role1 = new Role(null, "ROLE_USER");
            Role role2 = new Role(null, "ROLE_ADMIN");


            // Since you're saving an employee that references a role that hasn't been saved to the database, you need to add CascadeType.ALL to the Role property in the Employee Class
            Employee employee1 = new Employee(1L, "Brandon", "brandonalfa@", "Real", "Software Engineer", role1);
            employeeService.saveEmployee(employee1);

            Employee employee2 = new Employee(2L, "Jacob", "MillerLite@com", "Deal", "Product Owner", role2);
            employeeService.saveEmployee(employee2);

        };
    }

    @Bean
    CommandLineRunner runTask(TaskService taskService) {
        return args -> {
            Role role1 = new Role(null, "ROLE_USER");
            Employee employee1 = new Employee(1L, "Dalton", "brandonalfa@", "Real", "Software Engineer", role1);
            Task task1 = new Task(1L, "CI/CD methods brainstorm", employee1);
            taskService.saveTask(task1);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
