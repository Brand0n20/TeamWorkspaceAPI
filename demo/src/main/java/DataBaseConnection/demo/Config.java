package DataBaseConnection.demo;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeRepo;
import DataBaseConnection.demo.Employee.EmployeeService;
import DataBaseConnection.demo.Role.Role;
import DataBaseConnection.demo.Role.RoleRepo;
import DataBaseConnection.demo.Task.Task;
import DataBaseConnection.demo.Task.TaskRepo;
import DataBaseConnection.demo.Task.TaskService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Configuration
public class Config {

    private EmployeeRepo employeeRepo;

    private RoleRepo roleRepo;

    private TaskRepo taskRepo;

    private EntityManager entityManager;

    public Config(EmployeeRepo employeeRepo, RoleRepo roleRepo, TaskRepo taskRepo, EntityManager entityManager) {
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
        this.taskRepo = taskRepo;
        this.entityManager = entityManager;
    }

    @Bean
    CommandLineRunner runEmployee(EmployeeService employeeService, TaskService taskService) {
        return args -> {
            taskRepo.deleteAll();
            employeeRepo.deleteAll();
            roleRepo.deleteAll();
            Role role1 = roleRepo.findByName("USER");
            Role role2 = roleRepo.findByName("ADMIN");
            if (role1 == null || role2 == null) {
                role1 = new Role("USER");
                role2 = new Role("ADMIN");
                roleRepo.saveAll(Arrays.asList(role1, role2));
            }



            Employee employee1 = new Employee("Brandon Alfaro", "brandonalfa@", "Real", "Software Engineer", role1);
            employeeService.saveEmployee(employee1);

            Employee employee2 = new Employee("Jacob Miller", "MillerLite@com", "Deal", "Product Owner", role2);
            employeeService.saveEmployee(employee2);
            Employee existingEmployee = employeeService.getEmployee(employee1.getEmail());
            Employee existingEmployee2 = employeeService.getEmployee(employee2.getEmail());
            Task task1 = new Task("CI/CD methods brainstorm", "11/03/2023", existingEmployee.getEmail(), existingEmployee);
            taskService.saveTask(task1);
            Task task2  = new Task("Integrate IdentityNow Role mapping to roles in ServiceNow", "01/30/2024", existingEmployee2.getEmail(), existingEmployee2);
            Task task3 = new Task("Meet with BSA to discuss new ETL tool", "12/15/2023", existingEmployee.getEmail(), existingEmployee);
            taskService.saveTask(task2);
            taskService.saveTask(task3);
        };
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}