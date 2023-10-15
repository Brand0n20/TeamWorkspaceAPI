package DataBaseConnection.demo.Employee;

import DataBaseConnection.demo.Role.RoleRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    @Autowired
    private final EmployeeRepo employeeRepo;

    @Autowired
    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveEmployee(Employee employee) {
        log.info("saving new employee " + employee.getName());
        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // encoding password before saving to database
        employeeRepo.save(employee);
    }

    @Override
    public void addTaskToUser(String email, String name) {

    }

    @Override
    public List<Employee> getEmployees() {
        log.info("Fetching all employees");
        return employeeRepo.findAll();
    }

    @Override
    public Employee getEmployee(String email) {
        log.info("Fetching employee");
        return employeeRepo.findByEmail(email);
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
