package DataBaseConnection.demo.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    void deleteByEmail(String email);

    void deleteAll();

}
