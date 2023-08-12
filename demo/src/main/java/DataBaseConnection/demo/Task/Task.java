package DataBaseConnection.demo.Task;

import DataBaseConnection.demo.Employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne  // by using OneToMany and ManyToOne, we are creating a bidirectional relationship. Meaning both classes can reference the other
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
