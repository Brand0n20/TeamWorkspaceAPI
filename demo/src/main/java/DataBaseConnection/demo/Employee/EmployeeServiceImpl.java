package DataBaseConnection.demo.Employee;

import DataBaseConnection.demo.Role.Role;
import DataBaseConnection.demo.Role.RoleRepo;
import DataBaseConnection.demo.Task.Task;
import DataBaseConnection.demo.Task.TaskRepo;
import Exceptions.BadRequest;
import Exceptions.ResourceNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    @Autowired
    private final EmployeeRepo employeeRepo;

    @Autowired
    private final RoleRepo roleRepo;

    @Autowired
    private final TaskRepo taskRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> getEmployees() {
        try {
            log.info("Fetching all employees");
            return employeeRepo.findAll();
        } catch (Exception e) {
            throw new NoSuchElementException("No employees found");
        }

    }

    @Override
    public List<Task> getEmployeeTasks(String email) {
        Employee existingEmployee = employeeRepo.findByEmail(email);
        List<Task> taskList = taskRepo.findTasksByEmployeeEmail(existingEmployee.getEmail());
        return taskList;
    }

    @Override
    public Employee getEmployee(String email) {

        Employee employee;
        try {
            log.info("Fetching employee");
            employee = employeeRepo.findByEmail(email);
        } catch (Exception e) {
            throw new NoSuchElementException("No employee with that email found");
        }
        if (employee == null) {
            log.error("Employee with email: " + email + " does not exist in the database");
            throw new NoSuchElementException();
        } else {
            return employee;
        }
    }


    @Override
    public void addTaskToEmployee(String email, String name) {

    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Employee existingEmployee = employeeRepo.findByEmail(employee.getEmail());
        Role existingRole = roleRepo.findByName(employee.getRole().getName());
        if (existingEmployee == null) {
                if (existingRole != null) {
                    employee.setRole(existingRole);
                    try {
                        log.info("saving new employee " + employee.getName());
                        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // encoding password before saving to database
                        return employeeRepo.save(employee);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    log.error("This role doesn't exist");
                    throw new ResourceNotFoundException();
                }
        } else {
            log.error("That email is already in use");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That email is already in use");
        }
    }

    @Override
    public Employee updateEmployee(String email, Employee employee) {

        Employee existingEmployee = employeeRepo.findByEmail(email);
        Employee otherEmployee = employeeRepo.findByEmail(employee.getEmail());
        if (existingEmployee == null) {
            log.error("Employee doesn't exist");
            throw new ResourceNotFoundException("Employee with email: " + employee.getEmail() + " doesn't exist");
        } else {
            if ((Objects.equals(existingEmployee.getEmail(), employee.getEmail()) &&
                    Objects.equals(otherEmployee.getId(), existingEmployee.getId())) || otherEmployee == null) {
                if (employee.getId() == null) {
                    employee.setId(existingEmployee.getId());
                } else if (!Objects.equals(employee.getId(), existingEmployee.getId())) {
                    log.error("Employee id cannot be changed");
                    throw new BadRequest("Employee id cannot be changed");
                } else if (employee.getEmail().isEmpty()) {
                    log.error("Employee email cannot be empty");
                    throw new BadRequest("Employee email cannot be empty");
                }
                try {
                    // Update existing employee's fields in one line
                    // BeanUtils class provides methods to copy the properties from the source object to the target object.
                    BeanUtils.copyProperties(employee, existingEmployee, "id"); // Exclude copying "id" property
                    employeeRepo.save(existingEmployee);

                } catch (Exception e) {
                    log.error("Unable to update employee");
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                log.error("The updated employee's email is already in use by someone else");
                throw new BadRequest("The updated employee's email is already in use by someone else");
            }
        }

        return existingEmployee;
    }

    @Override
    public void deleteEmployee(String email) {
        Employee existingEmployee = employeeRepo.findByEmail(email);
        if (existingEmployee != null) {
            try {
                log.info("Deleting employee by their email");
                employeeRepo.deleteByEmail(existingEmployee.getEmail());
            } catch (Exception e) {
                throw new RuntimeException("Unable to delete employee");
            }
        } else {
            throw new NoSuchElementException("Employee email doesn't exist in our database");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(username);
        if (employee == null) {
            log.error("Employee not found");
            throw new UsernameNotFoundException("Employee not found");
        } else {
            log.info("Employee found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(employee.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(), employee.getPassword(), authorities);
    }
}
