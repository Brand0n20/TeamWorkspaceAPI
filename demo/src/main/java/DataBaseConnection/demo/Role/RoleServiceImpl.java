package DataBaseConnection.demo.Role;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeRepo;
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
public class RoleServiceImpl implements RoleService{

    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public Role findByName(Role role) {
        return null;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String employeeEmail, Role role) {
        Employee employee = employeeRepo.findByEmail(employeeEmail);
        Role role1 = roleRepo.findByName(role.getName());
        if (employee.getRole() == null) {
            log.info("Adding role to user");
            employee.setRole(role1);
        }
    }
}
