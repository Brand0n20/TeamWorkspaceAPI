package DataBaseConnection.demo.Task;

import DataBaseConnection.demo.Employee.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String dueDate;

    private String employeeEmail;


    @ManyToOne(cascade = {CascadeType.MERGE})  // by using OneToMany and ManyToOne, we are creating a bidirectional relationship. Meaning both classes can reference the other
    @JoinColumn(name = "employee_id")
    private Employee employee;

public Task(String name, String dueDate, String employeeEmail, Employee employee) {
    this.name = name;
    this.dueDate = dueDate;
    this.employeeEmail = employeeEmail;
    this.employee = employee;
}

}
