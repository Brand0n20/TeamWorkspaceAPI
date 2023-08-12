package DataBaseConnection.demo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    RoleRepo roleRepo;

    @GetMapping("/roles")
    public void getRoles() {
        roleRepo.findAll();
    }
}
