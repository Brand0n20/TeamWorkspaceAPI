package DataBaseConnection.demo.Employee;

import DataBaseConnection.demo.Role.Role;
import DataBaseConnection.demo.Task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String name;
        private String email;
        private String password;
        private String jobTitle;

        @ManyToOne(cascade = {CascadeType.ALL})
        private Role role = new Role();

        @OneToMany(mappedBy = "employee")       // The mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent class in our child class.
        private final List<Task> tasks = new ArrayList<>();
}
