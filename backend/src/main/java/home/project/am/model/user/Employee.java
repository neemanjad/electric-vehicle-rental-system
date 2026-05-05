package home.project.am.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @Column(name = "USER_userName", nullable = false, length = 45)
    private String userName;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_userName", referencedColumnName = "userName", 
                foreignKey = @ForeignKey(name = "fk_EMPLOYEE_USER1"))
    private User user;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
