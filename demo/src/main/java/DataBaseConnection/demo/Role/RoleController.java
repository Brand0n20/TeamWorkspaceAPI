package DataBaseConnection.demo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class RoleController {

    @Autowired
    RoleRepo roleRepo;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {

        return new ResponseEntity<>(roleRepo.findAll(), HttpStatus.OK);
    }
}
