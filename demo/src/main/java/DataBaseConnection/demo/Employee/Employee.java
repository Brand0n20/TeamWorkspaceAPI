package DataBaseConnection.demo.Employee;

import DataBaseConnection.demo.Role.Role;
import DataBaseConnection.demo.Task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "employee")
@NoArgsConstructor
public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String name;

        @Column(unique = true)
        private String email;

        private String password;

        private String jobTitle;

        // Use CascadeType.MERGE if you're trying to create a new employee and the role has already been created in the db
        @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
        @JoinColumn(name = "role_id")
        private Role role;

        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)       // The mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent class in our child class.
        @JsonIgnore
        private List<Task> tasks = new ArrayList<>();

        public Employee(String name, String email, String password, String jobTitle, Role role) {
                this.name = name;
                this.email = email;
                this.password = password;
                this.jobTitle = jobTitle;
                this.role = role;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getJobTitle() {
                return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
                this.jobTitle = jobTitle;
        }

        public Role getRole() {
                return role;
        }

        public void setRole(Role role) {
                this.role = role;
        }

        public List<Task> getTasks() {
                return tasks;
        }
}
