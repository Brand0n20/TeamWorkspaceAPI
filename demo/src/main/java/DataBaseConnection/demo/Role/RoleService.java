package DataBaseConnection.demo.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();
    Role findByName(Role role);
    Role saveRole(Role role);
    void addRoleToUser(String employeeEmail, Role role);
}
